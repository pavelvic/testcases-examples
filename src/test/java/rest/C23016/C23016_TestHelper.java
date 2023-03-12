package rest.C23016;

public class C23016_TestHelper {
	private final ApiManager app;
	private final SubstitutionsSupplier substitutions;

	public C23016_TestHelper(ApiManager app) {
		this.app = app;
		this.substitutions = new SubstitutionsSupplier(app.db);
	}

	private final CalcPriceRCTByIdRequest referenceBody = CalcPriceRCTByIdRequest.builder()
			.weight("1.25")
			.colorIntensityId("9")
			.qualId("151") //!
			.quantityOfStones("1")
			.colorBaseId("12")
			.colorTone1Id("1")
			.colorTone2Id("1")
			.formId("1117")
			.build();

	public void verifyQualSubstitutions() {
		executeAndVerifySubstitutions(createOldNewBodysQuals(substitutions.getRctQualSubstitutions(), referenceBody));
	}

	public void verifyIntensitySubstitutions() {
		executeAndVerifySubstitutions(createOldNewBodysIntensity(substitutions.getRctIntensitySubstitutions(), referenceBody));
	}

	private void executeAndVerifySubstitutions(Map<CalcPriceRCTByIdRequest, CalcPriceRCTByIdRequest> oldNewBodys) {
		oldNewBodys.forEach((key, value) -> {
			var request = new ApiRequest<>(key);
			ApiResponse<CalcPriceRCTByIdResponse, CalcPriceRCTByIdResponse> oldResponse = app.api.calcPriceRCT(request);

			request = new ApiRequest<>(value);
			ApiResponse<CalcPriceRCTByIdResponse, CalcPriceRCTByIdResponse> newResponse = app.api.calcPriceRCT(request);

			verifyResponseCodeIs200(Arrays.asList(oldResponse, newResponse));
			assertThat("Для прайслиста РЦО стоимость по запросу " + oldResponse.getBody().getRequestParams() + " равна стоимости после переопределения по запросу " + newResponse.getBody().getRequestParams(),
					oldResponse.getBody().getPrice(), Is.is(newResponse.getBody().getPrice()));

		});
	}

	private void verifyResponseCodeIs200(List<ApiResponse<CalcPriceRCTByIdResponse, CalcPriceRCTByIdResponse>> responses) {
		responses.forEach(response -> assertThat("Для запроса " + response.getBody().getRequestParams() + " код ответа = 200",
				response.getCode(), Is.is(200)));
	}

	private Map<CalcPriceRCTByIdRequest, CalcPriceRCTByIdRequest> createOldNewBodysQuals (List<SubstitutionsSupplier.Substitution> subs, CalcPriceRCTByIdRequest body) {
		return subs.stream()
				.collect(Collectors.toMap(
						ps -> body.copy().setQualId(ps.getOldId()),
						ps -> body.copy().setQualId(ps.getNewId())));
	}

	private Map<CalcPriceRCTByIdRequest, CalcPriceRCTByIdRequest> createOldNewBodysIntensity (List<SubstitutionsSupplier.Substitution> subs, CalcPriceRCTByIdRequest body) {
		return subs.stream()
				.collect(Collectors.toMap(
						ps -> body.copy().setColorIntensityId(ps.getOldId()),
						ps -> body.copy().setColorIntensityId(ps.getNewId())));
	}
}
