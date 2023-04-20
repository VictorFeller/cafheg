Feature: Allocation Service

  As a service, it need to provide the parent's name having the rights to receive CAF.

  Scenario Outline: Parents ask for CAF
    Given parent 1 works at <Parent1WorkPlace> as <Parent1WorkType> for <Parent1Salary> and can parent the child <Parent1Parenting>
    And parent 2 works at <Parent2WorkPlace> as <Parent2WorkType> for <Parent2Salary> and can parent the child <Parent2Parenting>
    And parents live together ? <liveTogether>
    And child live in <ChildResidence> with parent <ChildLivesWithParent>
    When parents ask for CAF rights with <Scenario>
    Then  then parent who will receive the CAF is <ParentWithRights>

    Examples:
      | Scenario | Parent1WorkPlace | Parent1WorkType | Parent1Salary | Parent1Parenting |  | Parent2WorkPlace | Parent2WorkType | Parent2Salary | Parent2Parenting | ChildResidence | ChildLivesWithParent | liveTogether | ParentWithRights |
      | A        | FR               | independant     | 6700          | yes              |  | null             | null            | null          | true             | FR             | 1                    | true         | Parent1          |
      | B        | FR               | independant     | 6700          | no               |  | NE               | employee        | 3500          | true             | FR             | 2                    | false        | Parent2          |
      | C        | FR               | independant     | 6700          | yes              |  | NE               | employee        | 3500          | true             | FR             | 2                    | false        | Parent2          |
      | D        | FR               | independant     | 6700          | yes              |  | NE               | employee        | 3500          | true             | FR             | 1                    | true         | Parent1          |
      | E        | FR               | employe         | 6700          | yes              |  | NE               | employee        | 3500          | true             | FR             | 1                    | true         | Parent1          |
      | F        | FR               | independant     | 6700          | yes                |  | NE               | independant     | 3500          | true             | FR             | 1                    | true         | Parent1          |

