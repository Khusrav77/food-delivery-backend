package com.marketplace.identity.api.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public final class ClientIpResolver {

    private static final int MAX_FORWARDED_HOPS = 10;

    private final List<CidrRange> trustedProxies;

    public ClientIpResolver(
            @Value("${identity.otp.rate-limit.trusted-proxies:}") List<String> trustedProxies) {
        Objects.requireNonNull(trustedProxies, "trustedProxies must not be null");
        this.trustedProxies = trustedProxies.stream()
                .map(CidrRange::parse)
                .toList();
    }

    public String resolve(HttpServletRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        InetAddress remoteAddress = parseAddressOrNull(request.getRemoteAddr());
        if (remoteAddress == null) {
            return "unknown";
        }

        if (!isTrusted(remoteAddress)) {
            return remoteAddress.getHostAddress();
        }

        List<InetAddress> forwardedAddresses = parseForwardedAddresses(
                request.getHeader("X-Forwarded-For"));
        if (forwardedAddresses.isEmpty()) {
            return remoteAddress.getHostAddress();
        }

        forwardedAddresses.add(remoteAddress);
        for (int index = forwardedAddresses.size() - 1; index >= 0; index--) {
            InetAddress candidate = forwardedAddresses.get(index);
            if (!isTrusted(candidate)) {
                return candidate.getHostAddress();
            }
        }
        return forwardedAddresses.getFirst().getHostAddress();
    }

    private boolean isTrusted(InetAddress address) {
        return trustedProxies.stream().anyMatch(range -> range.contains(address));
    }

    private static List<InetAddress> parseForwardedAddresses(String header) {
        if (header == null || header.isBlank()) {
            return List.of();
        }
        String[] values = header.split(",");
        if (values.length > MAX_FORWARDED_HOPS) {
            return List.of();
        }

        List<InetAddress> addresses = new ArrayList<>(values.length);
        for (String value : values) {
            InetAddress address = parseAddressOrNull(value.trim());
            if (address == null) {
                return List.of();
            }
            addresses.add(address);
        }
        return addresses;
    }

    private static InetAddress parseAddressOrNull(String value) {
        if (value == null || value.isBlank() || !value.matches("[0-9a-fA-F:.]+")) {
            return null;
        }
        try {
            return InetAddress.getByName(value);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private record CidrRange(byte[] network, int prefixLength) {

        private static CidrRange parse(String value) {
            String[] parts = value.split("/", -1);
            InetAddress networkAddress = parseAddressOrNull(parts[0]);
            if (networkAddress == null || parts.length > 2) {
                throw new IllegalArgumentException("Invalid trusted proxy CIDR: " + value);
            }

            byte[] bytes = networkAddress.getAddress();
            int prefixLength = bytes.length * 8;
            if (parts.length == 2) {
                try {
                    prefixLength = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid trusted proxy CIDR: " + value, e);
                }
            }
            if (prefixLength < 0 || prefixLength > bytes.length * 8) {
                throw new IllegalArgumentException("Invalid trusted proxy CIDR: " + value);
            }
            return new CidrRange(bytes, prefixLength);
        }

        private boolean contains(InetAddress address) {
            byte[] candidate = address.getAddress();
            if (candidate.length != network.length) {
                return false;
            }
            int fullBytes = prefixLength / 8;
            int remainingBits = prefixLength % 8;
            for (int i = 0; i < fullBytes; i++) {
                if (network[i] != candidate[i]) {
                    return false;
                }
            }
            if (remainingBits == 0) {
                return true;
            }
            int mask = 0xFF << (8 - remainingBits);
            return (network[fullBytes] & mask) == (candidate[fullBytes] & mask);
        }
    }
}
