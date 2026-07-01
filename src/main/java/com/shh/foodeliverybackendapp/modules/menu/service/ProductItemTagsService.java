package com.shh.foodeliverybackendapp.modules.menu.service;

import java.util.UUID;

/**
 * Helper service that maintains the M2M between MenuItem and Tag.
 *
 * The public API surface is intentionally tiny: attach/detach by IDs and a
 * read for diagnostics. CRUD on the join row itself is not part of the
 * domain — clients should think in terms of menu items and tags, not join
 * records.
 */
public interface ProductItemTagsService {

    /** Idempotent: returns existing or creates new join row. */
    void attach(UUID menuItemId, UUID tagId);

    /** Idempotent: deletes the join row if it exists. */
    void detach(UUID menuItemId, UUID tagId);
}
