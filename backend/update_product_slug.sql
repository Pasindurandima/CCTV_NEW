UPDATE products
SET slug = CONCAT(
    LOWER(
        REPLACE(
            REPLACE(
                REPLACE(name, ' ', '-'),
                '/', '-'
            ),
            '.', ''
        )
    ),
    '-',
    id
)
WHERE slug IS NULL;