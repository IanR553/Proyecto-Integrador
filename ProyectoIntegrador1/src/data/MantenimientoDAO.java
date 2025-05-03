package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Mantenimiento;

public class MantenimientoDAO implements CRUD_operaciones<Mantenimiento, Integer> {

    private Connection connection;

    public MantenimientoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Mantenimiento mantenimiento) {
        String sql = "INSERT INTO Mantenimiento (id, motivo, tipo, fechaInicio, fechaFinalPropuesta) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, mantenimiento.getId());
            pstmt.setString(2, mantenimiento.getMotivo());
            pstmt.setString(3, mantenimiento.getTipo());

            LocalDate fechaInicio = mantenimiento.getFechaInicio();
            LocalDate fechaFinal = mantenimiento.getFechaFinalPropuesta();

            if (fechaInicio != null) {
                pstmt.setDate(4, Date.valueOf(fechaInicio));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            if (fechaFinal != null) {
                pstmt.setDate(5, Date.valueOf(fechaFinal));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Mantenimiento> fetch() {
        ArrayList<Mantenimiento> mantenimientos = new ArrayList<>();
        String sql = "SELECT * FROM Mantenimiento";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String motivo = rs.getString("motivo");
                String tipo = rs.getString("tipo");

                Date fechaInicioSQL = rs.getDate("fechaInicio");
                Date fechaFinalSQL = rs.getDate("fechaFinalPropuesta");

                LocalDate fechaInicio = (fechaInicioSQL != null) ? fechaInicioSQL.toLocalDate() : null;
                LocalDate fechaFinal = (fechaFinalSQL != null) ? fechaFinalSQL.toLocalDate() : null;

                Mantenimiento mantenimiento = new Mantenimiento(id, motivo, tipo, fechaInicio, fechaFinal);
                mantenimientos.add(mantenimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mantenimientos;
    }

    @Override
    public void update(Mantenimiento mantenimiento) {
        String sql = "UPDATE Mantenimiento SET motivo=?, tipo=?, fechaInicio=?, fechaFinalPropuesta=? WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mantenimiento.getMotivo());
            pstmt.setString(2, mantenimiento.getTipo());

            LocalDate fechaInicio = mantenimiento.getFechaInicio();
            LocalDate fechaFinal = mantenimiento.getFechaFinalPropuesta();

            if (fechaInicio != null) {
                pstmt.setDate(3, Date.valueOf(fechaInicio));
            } else {
                pstmt.setNull(3, java.sql.Types.DATE);
            }

            if (fechaFinal != null) {
                pstmt.setDate(4, Date.valueOf(fechaFinal));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }

            pstmt.setInt(5, mantenimiento.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Mantenimiento WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(Integer id) {
        String sql = "SELECT id FROM Mantenimiento WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id") == id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public void showAlert(String mensaje, String header, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle("Alerta");
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

