package controller;

import java.time.LocalDate;
import java.time.LocalTime;

import application.Main;
import data.HorarioDAO;
import data.ReservaDAO;
import data.ReservaEquipoDAO;
import data.ReservaSalaDAO;
import data.UserSession;
import javafx.scene.control.Alert;
import model.Equipo;
import model.Reserva;
import model.ReservaEquipo;
import model.ReservaSala;
import model.Sala;

public class ReservaFacade {
	
    private final HorarioDAO horarioDAO;
    private final ReservaDAO reservaDAO;
    private final ReservaSalaDAO reservaSalaDAO;
    private final ReservaEquipoDAO reservaEquipoDAO;
    
    public ReservaFacade(HorarioDAO horarioDAO, ReservaDAO reservaDAO,
                         ReservaSalaDAO reservaSalaDAO, ReservaEquipoDAO reservaEquipoDAO) {
        this.horarioDAO = horarioDAO;
        this.reservaDAO = reservaDAO;
        this.reservaSalaDAO = reservaSalaDAO;
        this.reservaEquipoDAO = reservaEquipoDAO;
    }

    public boolean realizarReserva(String horaInicioStr, String horaFinStr, LocalDate fecha,
                                    int semana, String tipoEquipo, Sala sala, Equipo equipo) {

        LocalTime horaInicio;
        LocalTime horaFin;

        try {
            horaInicio = LocalTime.parse(horaInicioStr);
            horaFin = LocalTime.parse(horaFinStr);
        } catch (Exception e) {
            Main.showAlert("Formato de hora incorrecto. Use HH:mm (ej. 14:00).", "Error de formato", Alert.AlertType.ERROR);
            return false;
        }

        String diaSemana = switch (fecha.getDayOfWeek()) {
            case MONDAY -> "lunes";
            case TUESDAY -> "martes";
            case WEDNESDAY -> "miercoles";
            case THURSDAY -> "jueves";
            case FRIDAY -> "viernes";
            case SATURDAY -> "sabado";
            case SUNDAY -> "domingo";
        };

        String idHorario = horarioDAO.traerIdHorario(semana, diaSemana, horaInicio, horaFin);

        if (idHorario == null) {
            Main.showAlert("No se encontró un horario con los datos proporcionados.", "Horario no encontrado", Alert.AlertType.WARNING);
            return false;
        }

        if (sala == null) {
            Main.showAlert("Debe seleccionar una sala para reservar.", "Sin selección", Alert.AlertType.WARNING);
            return false;
        }

        if (!"NINGUNO".equalsIgnoreCase(tipoEquipo) && equipo == null) {
            Main.showAlert("Debe seleccionar un equipo para reservar.", "Sin selección", Alert.AlertType.WARNING);
            return false;
        }

        long cedula = Long.parseLong(UserSession.getInstance().getUsername());

        Reserva reserva = new Reserva("Pendiente", "Sala", cedula, idHorario);
        reservaDAO.save(reserva);

        String idReserva = reservaDAO.traerIdPorCedulaYHorario(cedula, idHorario, "Sala");

        String idEquipo = "NINGUNO".equalsIgnoreCase(tipoEquipo) ? null : equipo.getId();

        ReservaSala reservaSala = new ReservaSala(idReserva, sala.getId(), idEquipo);

        if (!"NINGUNO".equalsIgnoreCase(tipoEquipo) && equipo != null) {
            ReservaEquipo reservaEquipo = new ReservaEquipo(idReserva, equipo.getId());
            reservaEquipoDAO.save(reservaEquipo);
        }

        if (reservaSalaDAO.save(reservaSala)) {
            Main.showAlert("Reserva realizada exitosamente.", "Éxito", Alert.AlertType.INFORMATION);
            return true;
        } else {
            Main.showAlert("No se pudo completar la reserva. Verifique disponibilidad o conflicto de horarios.", "Error", Alert.AlertType.ERROR);
            return false;
        }
    }
}
