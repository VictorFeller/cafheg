package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.common.Montant;
import ch.hearc.cafheg.business.versements.VersementAllocation;
import ch.hearc.cafheg.business.versements.VersementAllocationNaissance;
import ch.hearc.cafheg.business.versements.VersementParentEnfant;
import ch.hearc.cafheg.business.versements.VersementParentParMois;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersementMapper extends Mapper {

  private final String QUERY_FIND_ALL_ALLOCATIONS_NAISSANCE = "SELECT V.DATE_VERSEMENT,AN.MONTANT FROM VERSEMENTS V JOIN ALLOCATIONS_NAISSANCE AN ON V.NUMERO=AN.FK_VERSEMENTS";
  private final String QUERY_FIND_ALL_VERSEMENTS = "SELECT V.DATE_VERSEMENT,A.MONTANT FROM VERSEMENTS V JOIN VERSEMENTS_ALLOCATIONS VA ON V.NUMERO=VA.FK_VERSEMENTS JOIN ALLOCATIONS_ENFANTS AE ON AE.NUMERO=VA.FK_ALLOCATIONS_ENFANTS JOIN ALLOCATIONS A ON A.NUMERO=AE.FK_ALLOCATIONS";
  private final String QUERY_FIND_ALL_VERSEMENTS_PARENTS_ENFANTS = "SELECT AL.NUMERO AS PARENT_ID, E.NUMERO AS ENFANT_ID, A.MONTANT FROM VERSEMENTS V JOIN VERSEMENTS_ALLOCATIONS VA ON V.NUMERO=VA.FK_VERSEMENTS JOIN ALLOCATIONS_ENFANTS AE ON AE.NUMERO=VA.FK_ALLOCATIONS_ENFANTS JOIN ALLOCATIONS A ON A.NUMERO=AE.FK_ALLOCATIONS JOIN ALLOCATAIRES AL ON AL.NUMERO=V.FK_ALLOCATAIRES JOIN ENFANTS E ON E.NUMERO=AE.FK_ENFANTS";
  private final String QUERY_FIND_ALL_VERSEMENTS_PARENTS_ENFANTS_PAR_MOIS = "SELECT AL.NUMERO AS PARENT_ID, A.MONTANT, V.DATE_VERSEMENT, V.MOIS_VERSEMENT FROM VERSEMENTS V JOIN VERSEMENTS_ALLOCATIONS VA ON V.NUMERO=VA.FK_VERSEMENTS JOIN ALLOCATIONS_ENFANTS AE ON AE.NUMERO=VA.FK_ALLOCATIONS_ENFANTS JOIN ALLOCATIONS A ON A.NUMERO=AE.FK_ALLOCATIONS JOIN ALLOCATAIRES AL ON AL.NUMERO=V.FK_ALLOCATAIRES JOIN ENFANTS E ON E.NUMERO=AE.FK_ENFANTS";
  private final String QUERY_COUNT_VERSEMENTS_BY_ALLOCATAIREID = "SELECT COUNT(NUMERO) FROM VERSEMENTS WHERE FK_ALLOCATAIRES = ?";

  private static final Logger logger = LoggerFactory.getLogger(VersementMapper.class);
  //FIXME QUERY NOT WORKING java.lang.RuntimeException: org.h2.jdbc.JdbcSQLSyntaxErrorException: Colonne "AN.FK_VERSEMENTS" non trouvée
  public List<VersementAllocationNaissance> findAllVersementAllocationNaissance() {
    logger.debug("findAllVersementAllocationNaissance()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL_ALLOCATIONS_NAISSANCE)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        List<VersementAllocationNaissance> versements = new ArrayList<>();
        while (resultSet.next()) {
          logger.debug("resultSet#next");
          versements.add(
                  new VersementAllocationNaissance(new Montant(resultSet.getBigDecimal(2)),
                          resultSet.getDate(1).toLocalDate()));

        }
        return versements;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<VersementAllocation> findAllVersementAllocation() {
    logger.debug("findAllVersementAllocation()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL_VERSEMENTS)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        List<VersementAllocation> versements = new ArrayList<>();
        while (resultSet.next()) {
          logger.debug("resultSet#next");
          versements.add(
                  new VersementAllocation(new Montant(resultSet.getBigDecimal(2)),
                          resultSet.getDate(1).toLocalDate()));

        }
        return versements;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<VersementParentEnfant> findVersementParentEnfant() {
    logger.debug("findVersementParentEnfant()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL_VERSEMENTS_PARENTS_ENFANTS)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        List<VersementParentEnfant> versements = new ArrayList<>();
        logger.debug("resultSet#next");
        while (resultSet.next()) {
          versements.add(
                  new VersementParentEnfant(resultSet.getLong(1), resultSet.getLong(2),
                          new Montant(resultSet.getBigDecimal(3))));

        }
        return versements;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public List<VersementParentParMois> findVersementParentEnfantParMois() {
    logger.debug("findVersementParentEnfantParMois()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL_VERSEMENTS_PARENTS_ENFANTS_PAR_MOIS)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        List<VersementParentParMois> versements = new ArrayList<>();
        while (resultSet.next()) {
          logger.debug("resultSet#next");
          versements.add(
                  new VersementParentParMois(resultSet.getLong(1),
                          new Montant(resultSet.getBigDecimal(2)),
                          resultSet.getDate(3).toLocalDate(), resultSet.getDate(4).toLocalDate()));

        }
        return versements;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public int countVersementsByAllocataireId(int allocataireId) {
    int count = -1;
    logger.debug("findVersementsByAllocataireId()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_COUNT_VERSEMENTS_BY_ALLOCATAIREID)) {
      preparedStatement.setLong(1, allocataireId);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          count = rs.getInt(1);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return count;
  }

}
