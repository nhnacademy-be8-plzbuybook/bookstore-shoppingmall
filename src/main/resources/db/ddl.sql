# 주문
create table orders
(
    order_id           char(36)       not null,
    number             char(26)       not null,
    name               varchar(100)   not null,
    ordered_at         datetime       not null default current_timestamp,
    delivery_wish_date date           null,
    used_point         int            null     default 0,
    delivery_fee       decimal(10, 2) not null,
    order_price        decimal(5, 2)  not null,
    status             tinyint        not null,
    primary key (order_id)
);

# 비회원 주문
create table non_member_order
(
    non_member_order_id bigint       not null auto_increment,
    nmo_order_id        char(36)     not null,
    password            varchar(200) not null,
    primary key (non_member_order_id),
    foreign key (nmo_order_id) references orders (order_id) on delete restrict
);

# 회원 주문
create table member_order
(
    member_order_id bigint   not null auto_increment,
    mo_order_id     char(36) not null,
    mo_member_id    bigint   not null,
    primary key (member_order_id),
    foreign key (mo_order_id) references orders (order_id),
    foreign key (mo_member_id) references member (member_id) on delete restrict
);

# 주문 배송지
create table order_delivery
(
    order_delivery_id bigint      not null auto_increment,
    od_order_id       char(36)    not null,
    delivery_company  varchar(50) not null,
    tracking_number   varchar(30) not null unique,
    registered_at     datetime    not null default current_timestamp,
    primary key (order_delivery_id),
    foreign key (od_order_id) references orders (order_id) on delete cascade
);

# 주문배송
create table order_delivery_address
(
    order_delivery_address_id bigint       not null auto_increment,
    zip_code                  varchar(20)  not null,
    location_address          varchar(100) not null,
    detail_address            varchar(100) not null,
    recipient                 varchar(100) not null,
    recipient_phone           varchar(15)  not null,
    primary key (order_delivery_address_id)
);


# 주문상품
create table order_product
(
    order_product_id   bigint         not null auto_increment,
    op_selling_book_id bigint         not null,
    op_order_id        char(36)       not null,
    price              decimal(10, 2) not null,
    quantity           int            not null default 1 check ( quantity >= 1 ),
    coupon_discount    decimal(10, 2) null check (coupon_discount >= 0),
    status             tinyint        not null,
    primary key (order_product_id),
    foreign key (op_selling_book_id) references selling_book (selling_book_id) on delete restrict,
    foreign key (op_order_id) references orders (order_id) on delete cascade
);

# 주문상품 포장
create table order_product_wrapping
(
    order_product_wrapping_id bigint not null auto_increment,
    opw_order_product_id      bigint not null,
    opw_wrapping_paper_id     bigint not null,
    quantity                  int    not null default 1 check ( quantity >= 1 ),
    primary key (order_product_wrapping_id),
    foreign key (opw_order_product_id) references order_product (order_product_id) on delete cascade,
    foreign key (opw_wrapping_paper_id) references wrapping_paper (wrapping_paper_id) on delete restrict
);

# 결제
create table payment
(
    payment_id        bigint         not null auto_increment,
    status            int            not null,
    payment_key       varchar(200)   not null,
    paid_at           datetime       not null default current_timestamp,
    amount            decimal(20, 2) not null check ( amount >= 0 ),
    method            varchar(50)    not null,
    easy_pay_provider varchar(100)   null,
    primary key (payment_id)
);