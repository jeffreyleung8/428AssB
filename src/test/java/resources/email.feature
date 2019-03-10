Feature: Send email with file attachment

  Scenario Outline: Send an email with a file attachment that is less than 25MB
    Given I am logged in to gmail
    When I send an email to valid "<recipient>"
    And "<file>" is attached
    And "<message>" is the subject
    Then an email should be found in the sent folder to "<recipient>" with "<file>" attached with subject "<message>"


    Examples:
      | recipient                       | file       | message |
      | meetaznboy@gmail.com            | image1.png | Email#1 |
      | jeffrey.leung4@mail.mcgill.ca   | image2.png | Email#2 |
      | yfanny002@yahoo.ca              | image3.png | Email#3 |
      | bico14497@hotmail.com           | image4.png | Email#4 |
      | jeffreyleung1801@hotmail.fr     | image5.png | Email#5 |















