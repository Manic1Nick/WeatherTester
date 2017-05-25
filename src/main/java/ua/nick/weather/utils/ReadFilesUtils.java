package ua.nick.weather.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ua.nick.weather.model.Provider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReadFilesUtils {

    private String directoryName;

    public ReadFilesUtils(String directoryName) {
        this.directoryName = directoryName;
    }

    public List<String> listFiles() {

        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList == null || fList.length == 0)
            return new ArrayList<>();

        //get all the files from a directory
        return Arrays.stream(fList)
                .filter(File::isFile)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public Map<Provider, Map<String, String>> readJsonFromFiles() {
        Map<Provider, Map<String, String>> mapJsonByProviders = new HashMap<>();
        
        List<String> fileNames = listFiles();
        Map<Provider, Map<String, String>> mapFileUrlsByProviders =
                createMapFileUrlsByProviders(fileNames);

        for (Provider provider : mapFileUrlsByProviders.keySet()) {
            Map<String, String> fileUrls = mapFileUrlsByProviders.get(provider);

            Map<String, String> jsons = new HashMap<>();
            jsons.put("forecast", readJsonFromFile(fileUrls.get("forecast")));
            jsons.put("actual", readJsonFromFile(fileUrls.get("actual")));
            mapJsonByProviders.put(provider, jsons);
        }

        return mapJsonByProviders;
    }

    private Map<Provider, Map<String, String>> createMapFileUrlsByProviders(List<String> fileNames) {
        Map<Provider, Map<String, String>> mapFileUrlsByProviders = new HashMap<>();

        for (Provider provider : Provider.values()) {
            List<String> fileNamesProvider = fileNames.stream()
                    .filter(name -> name.toUpperCase().contains(provider.toString()))
                    .collect(Collectors.toList());

            mapFileUrlsByProviders.put(provider, createMapJsons(fileNamesProvider));
        }
        return mapFileUrlsByProviders;
    }

    private Map<String, String> createMapJsons(List<String> fileNamesProvider) {
        Map<String, String> fileUrls = new HashMap<>();

        for (String name : fileNamesProvider) {
            String filePath = directoryName + name;

            if (name.toLowerCase().contains("forecast"))
                fileUrls.put("forecast", filePath);
            else if (name.toLowerCase().contains("actual"))
                fileUrls.put("actual", filePath);
        }
        return fileUrls;
    }

    private String readJsonFromFile(String filePath) {
        String json = "";

        try {
            JSONParser parser = new JSONParser();
            File file = new File(filePath);
            Object obj = parser.parse(new FileReader(file.toString()));
            JSONObject jsonObject = (JSONObject) obj;
            json = jsonObject.toJSONString();

        } catch (IOException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static void main(String[] args) {
        String forecastExamplesDir = "/home/jessy/IdeaProjects/WeatherTester/src/test/filesForecastExamples/";
        ReadFilesUtils readFileUtils = new ReadFilesUtils(forecastExamplesDir);

        Map<Provider, Map<String, String>> map = readFileUtils.readJsonFromFiles();

        System.out.println("TOTAL SIZE is : " + map.keySet().size());

        for (Provider provider : map.keySet()) {
            for (String forecast : map.get(provider).keySet())
                System.out.println(map.get(provider).get(forecast));
        }

    }
}
