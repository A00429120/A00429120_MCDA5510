This solution is a simple web application that allows users to have a 
set of shipments associated with their customer account.

Please make a copy of this git repo in your repo called TechnicalTestWeb2
Please make updates in this file and to the code and push when complete( or end of class) 

Item 1: Setup..
There is script file located in the "SQL Script", directory.
Create a database and using SQL Management Console exedcute the script to populate the data.
Note: You will have to update the Web.config to reference your DB name/credentials

Item 2: 
There is one compilation error in the project fix it.

Please Answer:
What was it?  How did you fix?
had to remove the s from ServiceTypes


Item 3:
The Shipments link in the menu bar of the app is not working, 
it should like to the index page of the ShipmentsController 

Please Answer:
What was it?  How did you fix?
the url was wrong replace the shipppmnet with shipment

Item 4:
Make the name label for the Customer on the shipment object say "Customer Name" in all places

Please Answer:
What was required to fix?
rename in the model from first name to customer name

Item 5:
There is a bug in the code.
The estimated ship date must be at least 24 hours after the Date Ordered.  Fix


Please Answer:
What was wrong and what was required to fix?
the (result > 0) has to changed to (result < 0)

Item 6:
When Editing customer Nitin, the province appear as Quebec.  Not Ontario - the default for create.

Explain why it was not Ontario or NS?
because its Province/State is set as NS instead of Novascotia it has to be updated

Item 7:
Add a button to the right of the customer dropdown to open the add a new customer.
Hint: you can use @Html.ActionLink ( does not have to be a button)
Was not able to find a dropdown hence created a button in next to the action tag

Item 8:
Convert Text dates to date picker
have harcoded the value and it works fine :)