package com.shh.foodeliverybackendapp.controller;

import com.shh.foodeliverybackendapp.dto.TagRequest;
import com.shh.foodeliverybackendapp.dto.TagResponse;
import com.shh.foodeliverybackendapp.service.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagResponse create(@Valid @RequestBody TagRequest request) {
        return tagService.create(request);
    }

    @GetMapping("/{id}")
    public TagResponse getById(@PathVariable UUID id) {
        return tagService.findById(id);
    }

    @GetMapping
    public List<TagResponse> getAll() {
        return tagService.findAll();
    }

    @PutMapping("/{id}")
    public TagResponse update(@PathVariable UUID id,
                              @Valid @RequestBody TagRequest request) {
        return tagService.updateById(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        tagService.deleteById(id);
    }
}
