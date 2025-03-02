Brokerage Application

Setup Instructions
-----------------------------------------------------------
git clone https://github.com/your-repo/BrokerageApplication.git

cd BrokerageApplication

Build & Run
----------------------------------------
mvn clean install
mvn spring-boot:run


Run Tests
-------------------------------------------
mvn test

API Endpoints
------------------------
Authentication
-------------
POST /auth/login → User Login

POST /auth/register → User Registration

Orders
-------------
POST /customer/orders → Create new order

GET /customer/orders?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD → Get user orders within a date range

DELETE /customer/orders/{id} → Cancel an order

Admin Controls
----------------
GET /admin/pending-orders → List pending orders

POST /admin/match-orders → Match pending orders
