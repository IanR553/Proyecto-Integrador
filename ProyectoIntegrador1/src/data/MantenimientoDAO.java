package data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import model.Mantenimiento;

public class MantenimientoDAO implements CRUD_operaciones<Mantenimiento, String> {

    private Connection connection;

    public MantenimientoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Mantenimiento mantenimiento) {
        String sql = "INSERT INTO Mantenimiento (motivo, tipo, fechaInicio, fechaFinalPropuesta, cedUsuario) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mantenimiento.getMotivo());
            pstmt.setString(2, mantenimiento.getTipo());

            LocalDate fechaInicio = mantenimiento.getFechaInicio();
            LocalDate fechaFinal = mantenimiento.getFechaFinalPropuesta();

            if (fechaInicio != null) {
                pstmt.setDate(3, Date.valueOf(fechaInicio));
            } else {
                pstmt.setNull(3, Types.DATE);
            }

            if (fechaFinal != null) {
                pstmt.setDate(4, Date.valueOf(fechaFinal));
            } else {
                pstmt.setNull(4, Types.DATE);
            }

            pstmt.setLong(5, mantenimiento.getCedUsuario());

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
                String id = rs.getString("id");
                String motivo = rs.getString("motivo");
                String tipo = rs.getString("tipo");

                Date fechaInicioSQL = rs.getDate("fechaInicio");
                Date fechaFinalSQL = rs.getDate("fechaFinalPropuesta");

                LocalDate fechaInicio = (fechaInicioSQL != null) ? fechaInicioSQL.toLocalDate() : null;
                LocalDate fechaFinal = (fechaFinalSQL != null) ? fechaFinalSQL.toLocalDate() : null;

                long cedUsuario = rs.getLong("cedUsuario");

                Mantenimiento mantenimiento = new Mantenimiento(id, motivo, tipo, fechaInicio, fechaFinal, cedUsuario);
                mantenimientos.add(mantenimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mantenimientos;
    }

    @Override
    public void update(Mantenimiento mantenimiento) {
        String sql = "UPDATE Mantenimiento SET motivo = ?, tipo = ?, fechaInicio = ?, fechaFinalPropuesta = ?, cedUsuario = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mantenimiento.getMotivo());
            pstmt.setString(2, mantenimiento.getTipo());

            LocalDate fechaInicio = mantenimiento.getFechaInicio();
            LocalDate fechaFinal = mantenimiento.getFechaFinalPropuesta();

            if (fechaInicio != null) {
                pstmt.setDate(3, Date.valueOf(fechaInicio));
            } else {
                pstmt.setNull(3, Types.DATE);
            }

            if (fechaFinal != null) {
                pstmt.setDate(4, Date.valueOf(fechaFinal));
            } else {
                pstmt.setNull(4, Types.DATE);
            }

            pstmt.setLong(5, mantenimiento.getCedUsuario());
            pstmt.setString(6, mantenimiento.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Mantenimiento WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM Mantenimiento WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}


