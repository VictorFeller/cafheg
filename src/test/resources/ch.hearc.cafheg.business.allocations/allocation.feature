Feature: Allocation Service

  As a service, it need to provide the parent's name having the rights to receive CAF.


  Scenario Outline: Parents ask for CAF
    Given Parent1 <Parent1Name> works at <Parent1WorkPlace> as <Parent1WorkType> for <Parent1Salary> and can parent the child <Parent1Parenting>
    And Parent2 <Parent2Name> works at <Parent2WorkPlace> as <Parent2WorkType> for <Parent2Salary> and can parent the child <Parent2Parenting>
    And parents live together ? <liveTogether>
    And child live in <ChildResidence> with parent <ChildLivesWithParent>
    When parents ask for CAF rights with <Scenario>
    Then parent who will receive the CAF is <ParentWithRights>

    Examples:
      | Scenario | Parent1Name | Parent1WorkPlace | Parent1WorkType | Parent1Salary | Parent1Parenting | Parent2Name | Parent2WorkPlace | Parent2WorkType | Parent2Salary | Parent2Parenting | ChildResidence | ChildLivesWithParent | liveTogether | ParentWithRights |
      | 'A'      | 'Marcel'    | 'FR'             | 'independant'   | 6700          | 'true'           | 'Typhène'   | 'none'           | 'none'          | 0             | 'true'           | 'FR'           | '1'                  | 'true'       | 'Parent1'        |
      | 'B'      | 'Marcel'    | 'FR'             | 'independant'   | 6700          | 'false'          | 'Typhène'   | 'FR'             | 'employee'      | 1000          | 'true'           | 'FR'           | '2'                  | 'false'      | 'Parent2'        |
      | 'C'      | 'Marcel'    | 'FR'             | 'independant'   | 6700          | 'true'           | 'Typhène'   | 'FR'             | 'employee'      | 1000          | 'true'           | 'FR'           | '2'                  | 'false'      | 'Parent2'        |
      | 'D'      | 'Marcel'    | 'FR'             | 'independant'   | 6700          | 'true'           | 'Typhène'   | 'FR'             | 'employee'      | 1000          | 'true'           | 'FR'           | '1'                  | 'true'       | 'Parent1'        |
      | 'E'      | 'Marcel'    | 'FR'             | 'employe'       | 6700          | 'true'           | 'Typhène'   | 'FR'             | 'employee'      | 1000          | 'true'           | 'FR'           | '1'                  | 'true'       | 'Parent1'        |
      | 'F'      | 'Marcel'    | 'FR'             | 'independant'   | 6700          | 'true'           | 'Typhène'   | 'FR'             | 'employee'      | 1000          | 'true'           | 'FR'           | '1'                  | 'true'       | 'Parent1'        |

