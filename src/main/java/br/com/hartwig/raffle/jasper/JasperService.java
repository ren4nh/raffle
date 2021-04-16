package br.com.hartwig.raffle.jasper;

import br.com.hartwig.raffle.jasper.bean.RaffleDTO;
import br.com.hartwig.raffle.lambda.bean.GenerateRaffleRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JasperService {

    private GenerateRaffleRequest request;

    public byte[] generateRaffle() throws JRException, IOException {
        List<RaffleDTO> raffleDTOList = new ArrayList<>();
        IntStream.rangeClosed(1, request.getTotal()).forEach(number -> {
            raffleDTOList.add(RaffleDTO.builder().description(request.getDescription()).price(request.getPrice()).title(request.getTitle()).number(number).build());
        });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream("raffle.jasper")) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(stream, null, new JRBeanCollectionDataSource(raffleDTOList));
            JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
        }

        return baos.toByteArray();
    }


}
