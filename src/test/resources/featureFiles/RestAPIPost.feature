Feature: Verify the get post in the API
  Scenario Outline: Get all posts from the API
   Given Get call to "<url>"
   Then Response is "<statusCode>"
   And Matches the schema "<schemaFile>"

   Examples:
    | url    | statusCode | schemaFile|
    |/users  |   200      |list_schema.json|

 Scenario Outline: Get By ID from the API
  Given Get call to "<url>"
  Then Response is "<statusCode>"
  And Matches the schema "<schemaFile>"
  And Response email is "<email>", first name is "<first_name>" and last name is "<last_name>"

  Examples:
   | url     | statusCode | email                | first_name | last_name | schemaFile|
   |/users/1 |   200      |george.bluth@reqres.in| George     | Bluth     | get_by_id_schema.json |
   |/users/2 |   200      |janet.weaver@reqres.in| Janet      | Weaver    | get_by_id_schema.json |
   |/users/10|   200      |byron.fields@reqres.in| Byron      | Fields    | get_by_id_schema.json |
   #|users/999|   200      |       ""               |    ""        |     ""      |      ""                 |
 Scenario Outline: GetList  from the API
  Given Get call to "<url>"
  Then Response is "<statusCode>"
  And "<name>" is on the list

  Examples:
   | url | statusCode | name |
   |/unknown| 200 | blue turquoise|