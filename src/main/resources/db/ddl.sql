
# 주문배송 ddl
create table order_delivery
(
    order_delivery_id varchar(255) not null primary key,
    delivery_company  varchar(50)  not null,
    tracking_number   varchar(30)  not null unique,
    registered_at     datetime     not null default current_timestamp,
    foreign key (order_delivery_id) references orders (order_id)
);