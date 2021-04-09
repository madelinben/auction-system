# 5104COMP OO: eAuction System
University Group Project to develop a working eAuction console application prototype, using the OOAD solution model.

## Organisation:
***Annotation:***

Suitable and descriptive comments must explain method functionality and any assumptions, following the 
*JavaDocs notation format:*
```java
/**
* [METHOD_DESCRIPTION]
* @param    [PARAMETER_NAME]    [PARAMETER_DESCRIPTION]
* @return                       [RETURN_VALUE_DESCRIPTION]
* @see      [OBJECT_RETURN_VALUE]
**/
```

***Branches:***

Branch names must follow convention of `[TYPE]/[SPRINT-NAME]`.

*Branch Types:*
- Bugfix - Issue found during testing and development.
- Hotfix - Client has found an issue.
- Feature - New feature being added to system.

***Class Diagram:***

![Example Class Diagram](docs/class-diagram.png)

***Use Case Diagram:***

![Example Use Case Diagram](docs/use-case-diagram.png)

## Requirements:

***Users:***
- A user must be able to setup an account.
- Can act as either a Seller or Buyer.
- All users should be able to browse Auctions in progress.

***Buyers:***
- Select in progress Auctions.
- Place bids on any item whose Auction has not closed.

***Bids:***
- System enforces an upper/lower bidding increment of 20% and 10% of the starting price.

***Sellers:***
- Can start Auctions by listing an item to be sold.
    
***Auctions:***
- Only started when a Seller inputs data (item description, start and reserve price, closing date).
- System enforces a closing date ≤7 days from the current date 
- Once data has been provided, Auction is set to pending and the Seller must verify the listing before it starts.
- Each Auction keeps track of every bid made against an item.
- Buyer with the highest bid is informed of their victory if an items reserve price is met when an Auction closes.
- If items reserve price has not been met, all Buyers who made a bid are informed and the Auction is closed.

***Menu:***
- System must allow multiple users to conduct auctions electronically (using multi-threading).
- Both sellers and auctions may be temporarily blocked which prevents the sellers from logging on or the auctions from being browsed or bid upon respectively.

## Features:
* ***Menu System:***

* [x] Establish working directory and Class structure.

* ***Persistent Storage:***

* ***Account Management:***

* ***Auction Setup:***

* ***Bid Method:***

* ***System Verification:***

* ***Notified Feedback:***
