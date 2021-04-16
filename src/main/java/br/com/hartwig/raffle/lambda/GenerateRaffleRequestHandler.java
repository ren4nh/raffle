package br.com.hartwig.raffle.lambda;


import br.com.hartwig.raffle.helpers.JSON;
import br.com.hartwig.raffle.jasper.JasperService;
import br.com.hartwig.raffle.lambda.bean.GenerateRaffleRequest;
import br.com.hartwig.raffle.lambda.bean.GenerateRaffleResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Base64;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateRaffleRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setHeaders(Collections.singletonMap("Access-Control-Allow-Origin", "https://ren4nh.github.io"));

        try {
            GenerateRaffleRequest requestDTO = JSON.fromJson(request.getBody(), GenerateRaffleRequest.class);

            byte[] pdf = JasperService.builder().request(requestDTO).build().generateRaffle();

            String pdf64 = Base64.getEncoder().encodeToString(pdf);

            response.setStatusCode(200);
            response.setBody(JSON.toJson(GenerateRaffleResponse.builder().success(true).data(pdf64).message("Rifa gerada com sucesso e enviada para o email informado.").build()));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setBody(JSON.toJson(GenerateRaffleResponse.builder().success(false).message("Erro ao gerar rifa.").build()));
            Logger.getLogger(GenerateRaffleResponse.class.getSimpleName()).log(Level.SEVERE, "Erro geral ao gerar rifa. " + request.getBody(), e);
        }

        return response;
    }


}
