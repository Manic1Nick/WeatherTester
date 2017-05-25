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

    public ReadFilesUtils() {
    }

    public Map<Provider, Map<String, String>> readJsonFromFiles(String directoryName) {
        Map<Provider, Map<String, String>> mapJsonByProviders = new HashMap<>();
        
        List<String> fileNames = listFiles(directoryName);
        Map<Provider, Map<String, String>> mapFileUrlsByProviders =
                createMapFileUrlsByProviders(directoryName, fileNames);

        for (Provider provider : mapFileUrlsByProviders.keySet()) {
            Map<String, String> fileUrls = mapFileUrlsByProviders.get(provider);

            Map<String, String> jsons = new HashMap<>();
            jsons.put("forecast", readJsonFromFile(fileUrls.get("forecast")));
            jsons.put("actual", readJsonFromFile(fileUrls.get("actual")));
            mapJsonByProviders.put(provider, jsons);
        }

        return mapJsonByProviders;
    }

    private List<String> listFiles(String directoryName) {

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

    private Map<Provider, Map<String, String>> createMapFileUrlsByProviders(
                                    String directoryName, List<String> allFileNames) {
        Map<Provider, Map<String, String>> mapFileUrlsByProviders = new HashMap<>();

        for (Provider provider : Provider.values()) {
            List<String> fileNames = allFileNames.stream()
                    .filter(name -> name.toUpperCase().endsWith(provider.name()))
                    .collect(Collectors.toList());

            if (fileNames.size() > 0)
                mapFileUrlsByProviders.put(provider, createMapJsons(directoryName, fileNames));
        }
        return mapFileUrlsByProviders;
    }

    private Map<String, String> createMapJsons(String directoryName, List<String> fileNamesProvider) {
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
        ReadFilesUtils readFileUtils = new ReadFilesUtils();

        Map<Provider, Map<String, String>> map = readFileUtils.readJsonFromFiles("/home/jessy/IdeaProjects/WeatherTester/src/test/filesForecastExamples/");

        System.out.println("TOTAL SIZE is : " + map.keySet().size());

        for (Provider provider : map.keySet()) {
            for (String forecast : map.get(provider).keySet())
                System.out.println(map.get(provider).get(forecast));
        }

    }
}
