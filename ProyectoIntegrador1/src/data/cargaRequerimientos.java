package data;

import model.RequerimientoSalas;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class cargaRequerimientos {

    public static ArrayList<RequerimientoSalas> cargarDesdeExcel(String rutaArchivo) {
        ArrayList<RequerimientoSalas> lista = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet hoja = workbook.getSheetAt(0); // Asume la primera hoja
            for (Row fila : hoja) {
                if (fila.getRowNum() == 0) continue; // Saltar encabezado

                String dia = fila.getCell(0).getStringCellValue();
                int idSala = (int) fila.getCell(1).getNumericCellValue();
                LocalTime horaInicio = LocalTime.parse(fila.getCell(2).getStringCellValue());
                LocalTime horaFin = LocalTime.parse(fila.getCell(3).getStringCellValue());
                boolean ocupada = fila.getCell(4).getStringCellValue().equalsIgnoreCase("si");

                RequerimientoSalas r = new RequerimientoSalas(dia, idSala, horaInicio, horaFin, ocupada);
                lista.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
