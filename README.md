# Web-Based-Multi-User-Payment-Service-Using-JavaEE
**Objective** : To design and implement a web-based, multi-user payment service using Java Enterprise Edition (Java EE) technologies

**Requirements**
Each user has a single online account whose currency is selected upon registration. An initial pretend amount of money of 1,000 GBP is deposited to the user account on startup. A user can select to have their account in GB Pounds, US dollars or Euros. In that case, the system should make the appropriate conversion to assign the right initial amount of money .
A user can instruct the system to make a direct payment to another user. If this request is accepted (i.e. the recipient of the payment exists and there are enough funds), money is transferred (within a single Java EE transaction) to the recipient immediately. A user should be able to check for notifications regarding payments in their account.
A user can instruct the system to request payment from some other user. A user should be able to check about such notifications for requests for payment. They can reject the request, or, in response to it, make a payment to the requesting user.
Users can access all their transactions, that is, sent and received payments and requests for payments as well as their current account balance.
An administrator can see all user accounts and all transactions.
Currency conversion must be implemented by a separate RESTful web service. The actual exchange rates will be statically assigned (hard-coded) in the RESTful service source code.

**Report**
Complete report in "report.docx" file

**Video Walthrough**
In "walkthrough.mp4"
