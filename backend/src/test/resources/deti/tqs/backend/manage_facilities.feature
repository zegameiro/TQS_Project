Feature: Manage facilities

    Scenario: Create a facility
        When I navigate to the staff/admin page
        And I login as an admin
        And I click on "Admin Page"
        And I click on "Facilities"
        And I click on "Create Facility"
        And I fill in "Name" with "Facility 1"
        And I fill in "City" with "City 1"
        And I fill in "Street Name" with "Street Name 1"
        And I fill in "Postal Code" with "12345"
        And I fill in "Phone Number" with "123456789"
        And I fill in "Room Capacity" with "100"
        And I click on the button to "Create Facility" 
        Then I should see "Facility 1" in the list of facilities 

    Scenario: Edit a facility
        When I login as an admin
        And I click on "Admins Page"
        And I click on "Facilities"
        And I click on the edit button for "Lisbon Beauty Plaza"
        And I change "Postal Code" to "1250-100"
        Then I should see the facility "Lisbon Beauty Plaza" with the "Postal Code" "1250-100" in the list

    Scenario: Delete a facility
        When I login as an admin
        And I click on "Admins Page"
        And I click on "Facilities"
        And I click on the delete button for "Lisbon Beauty Plaza"
        Then I should not see the facility "Lisbon Beauty Plaza" in the list

