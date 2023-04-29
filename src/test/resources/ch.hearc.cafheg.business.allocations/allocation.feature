Feature: Allocation Service

  As a service, it need to provide the parent's name having the rights to receive CAF.


  Scenario Outline: Parents ask for CAF
    Given Parent1 works <Parent1ActiviteLucrative> at <Parent1WorkPlace> as <Parent1WorkType> for <Parent1Salary> lives in <Parent1Residence> and can parent the child <Parent1Parenting>
    And Parent2 works <Parent2ActiviteLucrative> at <Parent2WorkPlace> as <Parent2WorkType> for <Parent2Salary> lives in <Parent2Residence> and can parent the child <Parent2Parenting>
    And parents live together ? <liveTogether>
    And child live in <ChildResidence>
    When parents ask for CAF rights with information above
    Then parent who will receive the CAF is <ParentWithRights>

    Examples:
      | Parent1ActiviteLucrative | Parent1Residence | Parent1WorkPlace | Parent1WorkType | Parent1Salary | Parent1Parenting | Parent2ActiviteLucrative | Parent2Residence | Parent2WorkPlace | Parent2WorkType | Parent2Salary | Parent2Parenting | ChildResidence | liveTogether | ParentWithRights |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 6700          | 'true'           | 'false'                  | 'FR'             | 'none'           | 'none'          | 0             | 'true'           | 'FR'           | 'true'       | 'Parent1'        |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 6700          | 'true'           | 'true'                   | 'NE'             | 'NE'             | 'employe'      | 1000          | 'false'          | 'FR'           | 'false'      | 'Parent1'        |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 6700          | 'true'           | 'true'                   | 'NE'             | 'NE'             | 'employe'      | 1000          | 'true'           | 'NE'           | 'false'      | 'Parent2'        |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 6700          | 'true'           | 'true'                   | 'FR'             | 'NE'             | 'employe'      | 1000          | 'true'           | 'FR'           | 'true'       | 'Parent1'        |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 4000          | 'true'           | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 6700          | 'true'           | 'FR'           | 'true'       | 'Parent2'        |
      | 'true'                   | 'FR'             | 'FR'             | 'employe'      | 4000          | 'true'           | 'true'                   | 'FR'             | 'FR'             | 'independant'  | 6700          | 'true'           | 'FR'           | 'true'       | 'Parent1'        |
      | 'true'                   | 'FR'             | 'FR'             | 'independant'   | 4000          | 'true'           | 'true'                   | 'FR'             | 'FR'             | 'independant'  | 6700          | 'true'           | 'FR'           | 'true'       | 'Parent2'        |




