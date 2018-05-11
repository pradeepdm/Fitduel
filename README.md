# Fit Duel

##
Contributors
 Pradeep Devarabetta Mallikarjun - 822032361
 Rakshith Ramesh - 822054994

## Synopsis
 This application lets users to take part in the step challenges and the winner will be awarded with the cashprize. 
 Yes, this application motivates the users to stay fit and get paid at the same time.   

## Installation
  ### Pre-requisite 
  - Since the application uses the device accelerometer, application needs to be tested on a physical device and not the emulator
  
  ### Setup
  1. Load the application in Android Studio and install using ADB.
  
## Workflow
  - User can login to the application using either Facebook authentication or any other account.
  - After logging in user is directed to the Challenges page where in the information about the challenge is displayed.
  - User needs to have sufficient funds inorder to take part in the challenge. For that, application has an option to add funds to his/her account using the Stripe Payment Gateway service. The stripe payment is currently in test mode (due to privacy policy limitations). But, we can test the real use case by using any of the Stripe demo cards.
  - Now, to add funds you can use this card 16 digit: **4242424242424242** Exp:**12/19** CVV:**123** and do the transaction. Added funds will be reflected in the User profile page and also in the Funds page
  - User can now enroll and take part in the on going challenge. User will be awarded with the cash prize if he wins the challenge. A notification service will be running in the notification bar to keep the user posted about the challenge status.
  - If the user is not interested in any of the challenges, then he/she can directly withdraw funds from the application  which will be added back to the Users bank account.
  
## Implementation 

 - Using **Firebase Authentication** and **Realtime** database for user Authentication, to store user profile information and also to store the user challenge and other activity details.
 - Using **Heroku Cloud Platform** to host the backend **Stripe payment/transactions** implementation. On the backend technology used is **Spring Microservices on REST architecture**. The Heroku platform is deplyed using the GitHub pipeline service.
 - Using **Facebook services** to support Facebook login in the application.
 - Using **Stripe payment SDK** and other services to integrate secure payment gateway service into the application.
 - User and system communication implementation using the system notifications service. 
 
## API Reference 
   1. [Stripe API](https://stripe.com/)
   
   2. [Firebase](https://firebase.google.com/)
   
   3. [Facebook Authentication](https://developers.facebook.com)
   
   4. [Heroku](https://devcenter.heroku.com/)

## Limitations

  - Currently Stripe payment gateway service is in test mode and not in live mode as we require organization and other privacy information to go online. 
  But, application can be tested in Stripe test mode for both add and withdraw usecases.
  - When user wins the challenge, the cash prize and the new balance post challenge is just displayed in the application and is not a real transaction using Stripe payment service. This is because for stripe to add cash prize to
  to the user account, the owner of the company(app owner) should have his account registered with Stripe and the users also should get registered formally using the Stripe redirect gateway service.
  Again, to add to that Stripe service needs to go live for this to happen.
  - App should be running in the background inorder for the Accelerometer service to be alive and keep track of the User movements.
 
    


   
## Screenshots

## License
MIT
