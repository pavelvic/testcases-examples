package rest.C23014;

@SuiteName("1_SMOKE ТЕСТИРОВАНИЕ")
@TestName("09_Прейскуранты")
public class C23014 extends BaseApiTest {

	private C23014_TestHelper helper;

	@BeforeMethod
	@Step("Предусловия:")
	public void setUpC23014() {
		helper = new C23014_TestHelper(app);
	}

	@DisplayedName("API: Расчет стоимости цветных камней по идентификаторам")
	@Description(
		"<b>Precondition:</b>\n\n" +
		"<b>Steps:</b>\n\n" +
		"01 - <i>Ввести в \"Request body\" запрос:\n{\n\"date\": \"2020-04-01T09:06:19.186Z\",\n\"weight\": 1.25,\n\"colorintensityId\": 2,\n\"qualId\": 161,\n\"quantityOfStones\": 1,\n\"colorBaseId\": 2,\n\"colorTone1Id\": 1,\n\"colorTone2Id\": 1,\n\"formId\": 1117\n}\nЗапрос введен</i>\n\nОтправить запрос нажатием на кнопку \"Execute\"\n1. Вернулся ответ с кодом ответа \"404\" - Не найден прейскурант на переданную дату/в БД не найдено значение параметра.\n2. Параметры тела ответа:\n - \"status\". Значение \"error\".\n - массив JSON-объектов \"errorDescription\" со вложенными параметрами:\n     1. code: \"3\" - не найден прейскурант на переданную дату \n     2. \"message\".  \"pricelist for DATE not found\" - если не найден прейскурант на переданную дату, где DATE - значение параметра \"date\"\n - параметры, переданные в теле запроса.\n![](index.php?/attachments/get/7206)\n\n</i>\n\n" +
		"02 - <i>Ввести в \"Request body\" запрос, изменив значение 1 или нескольких параметров, так чтобы значение отсутствовало в БД (Например \"FormId\": 9999):\n{\n\"weight\": 1.25,\n\"colorintensityId\": 2,\n\"qualId\": 161,\n\"quantityOfStones\": 1,\n\"colorBaseId\": 2,\n\"colorTone1Id\": 1,\n\"colorTone2Id\": 1,\n\"formId\": 1117\n}\nЗапрос введен</i>\n\n" +
		"03 - <i>Отправить запрос нажатием на кнопку \"Execute\"\n1. Вернулся ответ с кодом ответа \"404\" - Не найден прейскурант на переданную дату/в БД не найдено значение параметра.\n2. Параметры тела ответа:\n - \"status\". Значение \"error\".\n - массив JSON-объектов \"errorDescription\" со вложенными параметрами:\n     1. Code: \"4\" - в БД не удалось найти базовую стоимость/коэффициент по параметрам\n     2.Message: \"<XXX> with params: <a,b,c ..> not found\" - если в БД не удалось найти базовую стоимость/коэффициент по параметрам, где <XXX> - наименование коэффициента, <a,b,c ..> - наименования параметров\n - параметры, переданные в теле запроса.\n![](index.php?/attachments/get/7207)\n</i>")
	@Test(description="23014: Не найден прейскурант на переданную дату/в БД не найдено значение параметра")
	@TmsLink("23014")
	public void C23014Test(){
		step_01();
		step_02();
		step_03();
	}

	@Step("Шаг 1:")
	private void step_01(){
		helper.prepareNoPriceListRequest();
	}

	@Step("Шаг 2:")
	private void step_02(){
		helper.executeNoPriceListRequest();
	}

	@Step("Шаг 3:")
	private void step_03(){
		helper.assertNotInDictionaryRequest();
	}
}
