Create TABLE if Not Exists currency
(
    ID       SERIAL Primary Key,
    Code     VARCHAR(3)  not null,
    FullName VARCHAR(50) not null,
    Sign     VARCHAR(5)  not null
);
CREATE TABLE If Not Exists exchange_rate
(
    ID               SERIAL PRIMARY KEY,
    BaseCurrencyId   INT           NOT NULL,
    TargetCurrencyId INT           NOT NULL,
    Rate             DECIMAL(6, 2) NOT NULL,
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currency (ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currency (ID)
);

INSERT INTO Currency (Code, FullName, Sign)
VALUES ('AUD', 'Австралийский доллар', 'A$'),
       ('EUR', 'Евро', '€'),
       ('USD', 'Американский доллар', '$'),
       ('RUB', 'Российский рубль', '₽');

INSERT INTO exchange_rate (BaseCurrencyId, TargetCurrencyId, Rate)
VALUES (1, 2, 0.67),  -- AUD к EUR
       (1, 3, 0.75),  -- AUD к USD
       (4, 2, 0.011), -- RUB к EUR
       (4, 3, 0.012), -- RUB к USD
       (2, 3, 1.18),  -- EUR к USD
       (2, 4, 90.2),  -- EUR к RUB
       (3, 4, 75.3); -- USD к RUB


