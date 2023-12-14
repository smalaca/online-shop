# Online Shop

---
### Description
- Platform let people or companies to sell their products.
- Platform do not allow to sell products like food, medicament, weapons, etc.
- Platform do not store the products, it serves as a proxy between current owner of the product and the potential buyer.
- Platform is not responsible for the products' delivery.
- Person do not need to have an account on the platform to purchase the product.

### High Level Requirements
The platform should support following requirements:
- Products management
  - only registered seller can add products:
    - there is an automatic or (if required) manual verification of the account before products will be available for buying
    - manual verification is time-constrained
  - products are available for buying only if verified against platform policies
  - information about quantitys of the products are based on information shared by the seller
  - seller can change information about the product at any time they want 
  - if the order is during processing newly introduced changes about the product are not applicable to it for 15 minutes 

- Buying products
  - searching mechanism available
  - product must be added to the cart before buying
  - customer do not need to buy all products in the cart
  - customer may have multiple carts 
  - cart can be shared with other customers
  - customer may buy as many products at once as they want

- Payment and Invoicing
  - Money paid for the products are transferred to platform account
  - Platform transfers money to the seller
  - Platform gets commission for each sale
  - Customer can ask to transfer money to seller only once they receive the product
  - Platform is not responsible for invoicing the sale 

- Discounts and Offers
  - Discount codes are supported
  - Seller can create offers for their products
  - Platform can create offers for products

- Notification
  - Mail, text messages and notification in the application are supported
  - Used to inform about activities in the platform and sending recommendations