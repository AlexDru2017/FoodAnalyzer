package usdaFood.usda;

/**
 * USDA Urls
 *
 */
public interface USDAUrl {
	String foodReportUrl = "https://api.nal.usda.gov/fdc/v1/foods?";
	String listsUrl = "https://api.nal.usda.gov/ndb/list?format=json&";
	String nutrientReportUrl = "http://api.nal.usda.gov/ndb/nutrients/?format=json&";
	String searchUrl = "https://api.nal.usda.gov/fdc/v1/search?format=json&";
}
