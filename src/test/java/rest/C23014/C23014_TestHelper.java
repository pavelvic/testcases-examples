package rest.C23014;

public class C23014_TestHelper {

	private final ApiManager app;
	private ApiRequest<CalcPriceRCTByIdRequest> noPriceListRequest;

	public C23014_TestHelper(ApiManager app) {
		this.app = app;
	}

	public void prepareNoPriceListRequest() {
		noPriceListRequest = new ApiRequest<>(CalcPriceRCTByIdRequest.builder()
				.date("2020-04-01T09:06:19.186Z")
				.weight("1.25")
				.colorIntensityId("2")
				.qualId("161")
				.quantityOfStones("1")
				.colorBaseId("2")
				.colorTone1Id("1")
				.colorTone2Id("1")
				.formId("1117")
				.build());
	}

	public void executeNoPriceListRequest() {
		ApiResponse<CalcPriceRCTByIdResponse, CalcPriceRCTByIdResponse404> noPriceListResponse = app.api.calcPriceRCT(noPriceListRequest, CalcPriceRCTByIdResponse404.class);
		verifyResponse(noPriceListRequest,noPriceListResponse, "pricelist for 01.04.2020 9:06:19 +00:00 not found",3);
	}

	public void assertNotInDictionaryRequest() {
		var body = CalcPriceRCTByIdRequest.builder()
				.weight("1.25")
				.colorIntensityId("2")
				.qualId("161")
				.quantityOfStones("1")
				.colorBaseId("2")
				.colorTone1Id("1")
				.colorTone2Id("1")
				.formId("1117")
				.build();

		var request = new ApiRequest<>(body.copy().setFormId("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetFormCoeffForRCT with params: priceListVersionId, FormId, ColorIntensityId, sizeId not found",4);
		request = new ApiRequest<>(body.copy().setColorTone2Id("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetColorToneCoeffForRCT with params: priceListVersionId, ColorBaseId, ColorTone2Id not found",4);
		request = new ApiRequest<>(body.copy().setColorTone1Id("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetColorToneCoeffForRCT with params: priceListVersionId, ColorBaseId, ColorTone1Id not found",4);
		request = new ApiRequest<>(body.copy().setColorBaseId("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetColorCoeffForRCT with params: priceListVersionId, ColorBaseId, ColorIntensityId, sizeId not found",4);
		request = new ApiRequest<>(body.copy().setQualId("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetBasicCaratPriceForRCT with params: priceListVersionId, ColorIntensityId, QualId, sizeId not found",4);
		request = new ApiRequest<>(body.copy().setColorIntensityId("9999"));
		verifyResponse(request, app.api.calcPriceRCT(request, CalcPriceRCTByIdResponse404.class), "GetBasicCaratPriceForRCT with params: priceListVersionId, ColorIntensityId, QualId, sizeId not found",4);
	}

	private void verifyResponse(ApiRequest<CalcPriceRCTByIdRequest> request, ApiResponse<CalcPriceRCTByIdResponse, CalcPriceRCTByIdResponse404> response, String message, int errorCode) {
		assertThat("Для запроса "+request.getBody()+", код ответа = '404'",
				response.getCode(), Is.is(404));
		assertThat("Для запроса "+request.getBody()+" статус ответа = 'error",
				response.getErrorBody().getStatus(), Is.is("error"));
		assertThat("По запросу"+ request.getBody()+ " имеется в ответе ошибка с кодом "+errorCode,
				response.getErrorBody().getErrorsDescriptions().get(0).getCode(), Is.is(errorCode));
		assertThat("По запросу "+request.getBody()+" имеется ошибка c сообщением "+message,
				response.getErrorBody().getErrorsDescriptions().get(0).getMessage(), Is.is(message));
	}
}
