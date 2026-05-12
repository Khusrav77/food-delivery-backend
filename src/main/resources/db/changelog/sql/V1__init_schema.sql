CREATE SCHEMA IF NOT EXISTS food_delivery;
-- TABLE: categories
CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    image_url VARCHAR,
    position INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- TABLE: products
CREATE TABLE products (
    id UUID PRIMARY KEY,
    category_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    position INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id)
            REFERENCES categories(id)
            ON DELETE CASCADE
);

-- TABLE: menu_items
CREATE TABLE menu_items (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    position INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_menu_items_product
        FOREIGN KEY (product_id)
            REFERENCES products(id)
             ON DELETE CASCADE
);

-- TABLE: menu_item_images
CREATE TABLE menu_item_images (
    id UUID PRIMARY KEY,
    menu_item_id UUID NOT NULL,
    url VARCHAR NOT NULL,
    position INT NOT NULL DEFAULT 0,

    CONSTRAINT fk_menu_item_images_menu_item
        FOREIGN KEY (menu_item_id)
            REFERENCES menu_items(id)
            ON DELETE CASCADE
);

-- TABLE: menu_item_sizes
CREATE TABLE menu_item_sizes (
    id UUID PRIMARY KEY,
    menu_item_id UUID NOT NULL,
    size_type VARCHAR NOT NULL,
    size_value NUMERIC NOT NULL,
    size_unit VARCHAR NOT NULL,

    CONSTRAINT fk_menu_item_sizes_menu_item
        FOREIGN KEY (menu_item_id)
            REFERENCES menu_items(id)
            ON DELETE CASCADE
);

-- TABLE: menu_item_tags (M2M)
CREATE TABLE menu_item_tags (
    menu_item_id UUID NOT NULL,
    tag_id UUID NOT NULL,

    PRIMARY KEY (menu_item_id, tag_id),

    CONSTRAINT fk_menu_item_tags_menu_item
        FOREIGN KEY (menu_item_id)
            REFERENCES menu_items(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_menu_item_tags_tag
        FOREIGN KEY (tag_id)
            REFERENCES tags(id)
            ON DELETE CASCADE
);

-- TABLE: tags
CREATE TABLE tags (
    id UUID PRIMARY KEY,
    label VARCHAR NOT NULL UNIQUE
);


-- INDEXES (опционально, но правильно для production)
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_menu_items_product_id ON menu_items(product_id);
CREATE INDEX idx_menu_item_images_menu_item_id ON menu_item_images(menu_item_id);
CREATE INDEX idx_menu_item_sizes_menu_item_id ON menu_item_sizes(menu_item_id);
CREATE INDEX idx_menu_item_tags_tag_id ON menu_item_tags(tag_id);
