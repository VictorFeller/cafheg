package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllocataireMapper extends Mapper {

  private final VersementMapper versementMapper;
  private static final String QUERY_FIND_ALL = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES";
  private static final String QUERY_FIND_WHERE_NOM_LIKE = "SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?";
  private static final String QUERY_FIND_WHERE_NUMERO = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?";
  private static final String QUERY_FIND_WHERE_AVS = "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NO_AVS=?";
  private static final String QUERY_DELETE_BY_NUMERO = "DELETE FROM ALLOCATAIRES WHERE NUMERO = ?";
  private static final String QUERY_UPDATE_ALLOCATAIRE = "UPDATE ALLOCATAIRES SET NOM = ?, PRENOM = ? WHERE NO_AVS = ?";

  private static final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);

  public AllocataireMapper(VersementMapper versementMapper) {
    this.versementMapper = versementMapper;
  }

  public List<Allocataire> findAll() {
      logger.debug("findAll()");
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_ALL)) {
        logger.debug("SQL: " + QUERY_FIND_ALL);
        logger.debug("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();
        logger.debug("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
          logger.debug("Allocataire mapping");
        while (resultSet.next()) {
            logger.debug("ResultSet#next");
          allocataires
                  .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                          resultSet.getString(1), null, null, null, null, null, null));
        }
      }
        logger.debug("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
  }

  public List<Allocataire> findAllWhereNomLike(String likeNom) {
      logger.debug("findAllWhereNomLike() " + likeNom);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NOM_LIKE)) {
        logger.debug("SQL: " + QUERY_FIND_WHERE_NOM_LIKE);
      preparedStatement.setString(1, likeNom + "%");
        logger.debug("Allocation d'un nouveau tableau");
      List<Allocataire> allocataires = new ArrayList<>();
        logger.debug("Exécution de la requête");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
          logger.debug("Allocataire mapping");
        while (resultSet.next()) {
            logger.debug("ResultSet#next");
          allocataires
                  .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                          resultSet.getString(1), null, null, null, null, null, null));
        }
      }
        logger.debug("Allocataires trouvés " + allocataires.size());
      return allocataires;
    } catch (SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
  }


  public Allocataire findById(long id) {
      logger.debug("findById() " + id);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_NUMERO)) {
        logger.debug("SQL:" + QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setLong(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
          logger.debug("ResultSet#next");
        resultSet.next();
          logger.debug("Allocataire mapping");
        return new Allocataire(new NoAVS(resultSet.getString(1)),
                resultSet.getString(2), resultSet.getString(3), null, null, null, null, null, null);
      }
    } catch (SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findByAVS(NoAVS noAVS) {
      logger.debug("findById() " + noAVS.getValue());
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_FIND_WHERE_AVS)) {
        logger.debug("SQL:" + QUERY_FIND_WHERE_NUMERO);
      preparedStatement.setString(1, noAVS.getValue());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
          logger.debug("ResultSet#next");
        resultSet.next();
          logger.debug("Allocataire mapping");
        return new Allocataire(new NoAVS(resultSet.getString(1)),
                resultSet.getString(2), resultSet.getString(3), null, null, null, null, null, null);
      }
    } catch (SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
  }

  public void deleteById(int id) {
      logger.debug("deleteById() " + id);
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_DELETE_BY_NUMERO)) {
        logger.debug("SQL:" + QUERY_DELETE_BY_NUMERO);
        preparedStatement.setInt(1, id);
        int rowReturned = preparedStatement.executeUpdate();
        if(rowReturned != 1)
          throw new RuntimeException("Aucune ligne à supprimer");
    } catch (SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire update(Allocataire allocataire) {
      logger.debug("update() " + (allocataire.getNoAVS() != null ? allocataire.getNoAVS().getValue() : "null"));
    Connection connection = activeJDBCConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(QUERY_UPDATE_ALLOCATAIRE)) {
        preparedStatement.setString(1, allocataire.getNom());
        preparedStatement.setString(2, allocataire.getPrenom());
        preparedStatement.setString(3, allocataire.getNoAVS().value);
      preparedStatement.executeUpdate();
    } catch(SQLException e) {
      logger.error("SQL excpetion : ",e);
      throw new RuntimeException(e);
    }
    return this.findByAVS(allocataire.getNoAVS());
  }

}
