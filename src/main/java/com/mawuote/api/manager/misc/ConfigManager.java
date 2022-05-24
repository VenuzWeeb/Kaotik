package com.mawuote.api.manager.misc;

import com.mawuote.Kaotik;
import com.mawuote.api.manager.element.Element;
import com.mawuote.api.manager.friend.Friend;
import com.mawuote.api.manager.module.Module;
import com.mawuote.api.manager.module.ModuleCategory;
import com.mawuote.api.manager.value.Value;
import com.mawuote.api.manager.value.impl.*;
import com.google.gson.*;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {
    public void load(){
        try {
            loadModules();
            loadElements();
            loadPrefix();
            loadFriends();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void save(){
        try {
            if (!Files.exists(Paths.get("Kaotik/"))) Files.createDirectories(Paths.get("Kaotik/"));
            if (!Files.exists(Paths.get("Kaotik/Modules/"))) Files.createDirectories(Paths.get("Kaotik/Modules/"));
            if (!Files.exists(Paths.get("Kaotik/Elements/"))) Files.createDirectories(Paths.get("Kaotik/Elements/"));
            if (!Files.exists(Paths.get("Kaotik/Client/"))) Files.createDirectories(Paths.get("Kaotik/Client/"));

            for (ModuleCategory category : ModuleCategory.values()){
                if (category.equals(ModuleCategory.HUD)) continue;
                if (!Files.exists(Paths.get( "Kaotik/Modules/" + category.getName() + "/"))) Files.createDirectories(Paths.get("Kaotik/Modules/" + category.getName() + "/"));
            }

            saveModules();
            saveElements();
            savePrefix();
            saveFriends();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void attach(){
        Runtime.getRuntime().addShutdownHook(new SaveThread());
    }

    public void loadModules() throws IOException {
        for (Module<B> module : Kaotik.MODULE_MANAGER.getModules()){
            if (module.getCategory().equals(ModuleCategory.HUD)) continue;
            if (!Files.exists(Paths.get("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"))) continue;

            InputStream stream = Files.newInputStream(Paths.get("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"));
            JsonObject moduleJson;

            try {
                moduleJson = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
            } catch (IllegalStateException exception){
                exception.printStackTrace();
                Kaotik.LOGGER.error(module.getName());
                Kaotik.LOGGER.error("Bailing out. You are on your own. Good luck.");
                continue;
            }

            if (moduleJson.get("Name") == null || moduleJson.get("Status") == null) continue;
            if (moduleJson.get("Status").getAsBoolean()) module.enable();

            JsonObject valueJson = moduleJson.get("Values").getAsJsonObject();

            for (Value value : module.getValues()){
                JsonElement dataObject = valueJson.get(value.getName());
                if (dataObject != null && dataObject.isJsonPrimitive()){
                    if (value instanceof ValueBoolean){
                        ((ValueBoolean) value).setValue(dataObject.getAsBoolean());
                    } else if (value instanceof ValueNumber){
                        if (((ValueNumber) value).getType() == ValueNumber.INTEGER){
                            ((ValueNumber) value).setValue(dataObject.getAsInt());
                        } else if (((ValueNumber) value).getType() == ValueNumber.DOUBLE){
                            ((ValueNumber) value).setValue(dataObject.getAsDouble());
                        } else if (((ValueNumber) value).getType() == ValueNumber.FLOAT){
                            ((ValueNumber) value).setValue(dataObject.getAsFloat());
                        }
                    } else if (value instanceof ValueEnum){
                        ((ValueEnum) value).setValue(((ValueEnum) value).getEnumByName(dataObject.getAsString()));
                    } else if (value instanceof ValueString){
                        ((ValueString) value).setValue(dataObject.getAsString());
                    } else if (value instanceof ValueColor){
                        ((ValueColor) value).setValue(new Color(dataObject.getAsInt()));
                        if (valueJson.get(value.getName() + "-Rainbow") != null){
                            ((ValueColor) value).setRainbow(valueJson.get(value.getName() + "-Rainbow").getAsBoolean());
                        }
                        if (valueJson.get(value.getName() + "-Alpha") != null){
                            ((ValueColor) value).setValue(new Color(((ValueColor) value).getValue().getRed(), ((ValueColor) value).getValue().getGreen(), ((ValueColor) value).getValue().getBlue(), valueJson.get(value.getName() + "-Alpha").getAsInt()));
                        }
                    } else if (value instanceof ValueBind){
                        ((ValueBind) value).setValue(dataObject.getAsInt());
                    }
                }
            }

            stream.close();
        }
    }

    public void saveModules() throws IOException {
        for (Module<B> module : Kaotik.MODULE_MANAGER.getModules()){
            if (module.getCategory().equals(ModuleCategory.HUD)) continue;

            if (Files.exists(Paths.get("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"))) {
                File file = new File("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json");
                file.delete();
            }

            Files.createFile(Paths.get("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject moduleJson = new JsonObject();
            JsonObject valueJson = new JsonObject();

            moduleJson.add("Name", new JsonPrimitive(module.getName()));
            moduleJson.add("Status", new JsonPrimitive(module.isToggled()));

            for (Value value : module.getValues()){
                if (value instanceof ValueBoolean){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueBoolean) value).getValue()));
                } else if (value instanceof ValueNumber){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueNumber) value).getValue()));
                } else if (value instanceof ValueEnum){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueEnum) value).getValue().name()));
                } else if (value instanceof ValueString){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueString) value).getValue()));
                } else if (value instanceof ValueColor){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueColor) value).getValue().getRGB()));
                    valueJson.add(value.getName() + "-Alpha", new JsonPrimitive(((ValueColor) value).getValue().getAlpha()));
                    valueJson.add(value.getName() + "-Rainbow", new JsonPrimitive(((ValueColor) value).getRainbow()));
                } else if (value instanceof ValueBind){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueBind) value).getValue()));
                }
            }

            moduleJson.add("Values", valueJson);

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Kaotik/Modules/" + module.getCategory().getName() + "/" + module.getName() + ".json"), StandardCharsets.UTF_8);
            writer.write(gson.toJson(new JsonParser().parse(moduleJson.toString())));
            writer.close();
        }
    }

    public void loadElements() throws IOException {
        for (Element element : Kaotik.ELEMENT_MANAGER.getElements()){
            if (!Files.exists(Paths.get("Kaotik/Elements/" + element.getName() + ".json"))) continue;

            InputStream stream = Files.newInputStream(Paths.get("Kaotik/Elements/" + element.getName() + ".json"));
            JsonObject elementJson = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();

            if (elementJson.get("Name") == null || elementJson.get("Status") == null || elementJson.get("Positions") == null) continue;
            if (elementJson.get("Status").getAsBoolean()) element.enable();

            JsonObject valueJson = elementJson.get("Values").getAsJsonObject();
            JsonObject positionJson = elementJson.get("Positions").getAsJsonObject();

            for (Value value : element.getValues()){
                JsonElement dataObject = valueJson.get(value.getName());
                if (dataObject != null && dataObject.isJsonPrimitive()){
                    if (value instanceof ValueBoolean){
                        ((ValueBoolean) value).setValue(dataObject.getAsBoolean());
                    } else if (value instanceof ValueNumber){
                        if (((ValueNumber) value).getType() == ValueNumber.INTEGER){
                            ((ValueNumber) value).setValue(dataObject.getAsInt());
                        } else if (((ValueNumber) value).getType() == ValueNumber.DOUBLE){
                            ((ValueNumber) value).setValue(dataObject.getAsDouble());
                        } else if (((ValueNumber) value).getType() == ValueNumber.FLOAT){
                            ((ValueNumber) value).setValue(dataObject.getAsFloat());
                        }
                    } else if (value instanceof ValueEnum){
                        ((ValueEnum) value).setValue(((ValueEnum) value).getEnumByName(dataObject.getAsString()));
                    } else if (value instanceof ValueString){
                        ((ValueString) value).setValue(dataObject.getAsString());
                    } else if (value instanceof ValueColor){
                        ((ValueColor) value).setValue(new Color(dataObject.getAsInt()));
                        if (valueJson.get(value.getName() + "-Rainbow") != null){
                            ((ValueColor) value).setRainbow(valueJson.get(value.getName() + "-Rainbow").getAsBoolean());
                        }
                        if (valueJson.get(value.getName() + "-Alpha") != null){
                            ((ValueColor) value).setValue(new Color(((ValueColor) value).getValue().getRed(), ((ValueColor) value).getValue().getGreen(), ((ValueColor) value).getValue().getBlue(), valueJson.get(value.getName() + "-Alpha").getAsInt()));
                        }
                    } else if (value instanceof ValueBind){
                        ((ValueBind) value).setValue(dataObject.getAsInt());
                    }
                }
            }

            if (positionJson.get("X") != null && positionJson.get("Y") != null) {
                element.frame.setX(positionJson.get("X").getAsFloat());
                element.frame.setY(positionJson.get("Y").getAsFloat());
            }

            stream.close();
        }
    }

    public void saveElements() throws IOException {
        for (Element element : Kaotik.ELEMENT_MANAGER.getElements()){
            if (Files.exists(Paths.get("Kaotik/Elements/" + element.getName() + ".json"))) {
                File file = new File("Kaotik/Elements/" + element.getName() + ".json");
                file.delete();
            }

            Files.createFile(Paths.get("Kaotik/Elements/" + element.getName() + ".json"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject elementJson = new JsonObject();
            JsonObject valueJson = new JsonObject();
            JsonObject positionJson = new JsonObject();

            elementJson.add("Name", new JsonPrimitive(element.getName()));
            elementJson.add("Status", new JsonPrimitive(element.isToggled()));

            for (Value value : element.getValues()){
                if (value instanceof ValueBoolean){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueBoolean) value).getValue()));
                } else if (value instanceof ValueNumber){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueNumber) value).getValue()));
                } else if (value instanceof ValueEnum){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueEnum) value).getValue().name()));
                } else if (value instanceof ValueString){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueString) value).getValue()));
                } else if (value instanceof ValueColor){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueColor) value).getValue().getRGB()));
                    valueJson.add(value.getName() + "-Alpha", new JsonPrimitive(((ValueColor) value).getValue().getAlpha()));
                    valueJson.add(value.getName() + "-Rainbow", new JsonPrimitive(((ValueColor) value).getRainbow()));
                } else if (value instanceof ValueBind){
                    valueJson.add(value.getName(), new JsonPrimitive(((ValueBind) value).getValue()));
                }
            }

            positionJson.add("X", new JsonPrimitive(element.frame.getX()));
            positionJson.add("Y", new JsonPrimitive(element.frame.getY()));

            elementJson.add("Values", valueJson);
            elementJson.add("Positions", positionJson);

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Kaotik/Elements/" + element.getName() + ".json"), StandardCharsets.UTF_8);
            writer.write(gson.toJson(new JsonParser().parse(elementJson.toString())));
            writer.close();
        }
    }

    public void loadPrefix() throws IOException {
        if (!Files.exists(Paths.get("Kaotik/Client/Prefix.json"))) return;

        InputStream stream = Files.newInputStream(Paths.get("Kaotik/Client/Prefix.json"));
        JsonObject prefixJson = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();

        if (prefixJson.get("Prefix") == null) return;
        Kaotik.COMMAND_MANAGER.setPrefix(prefixJson.get("Prefix").getAsString());

        stream.close();
    }

    public void savePrefix() throws IOException {
        if (Files.exists(Paths.get("Kaotik/Client/Prefix.json"))) {
            File file = new File("Kaotik/Client/Prefix.json");
            file.delete();
        }

        Files.createFile(Paths.get("Kaotik/Client/Prefix.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject prefixJson = new JsonObject();

        prefixJson.add("Prefix", new JsonPrimitive(Kaotik.COMMAND_MANAGER.getPrefix()));

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Kaotik/Client/Prefix.json"), StandardCharsets.UTF_8);
        writer.write(gson.toJson(new JsonParser().parse(prefixJson.toString())));
        writer.close();
    }

    public void loadFriends() throws IOException {
        if (!Files.exists(Paths.get("Kaotik/Client/Friends.json"))) return;

        InputStream stream = Files.newInputStream(Paths.get("Kaotik/Client/Friends.json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();

        if (mainObject.get("Friends") == null) return;
        JsonArray friendArray = mainObject.get("Friends").getAsJsonArray();
        friendArray.forEach(friend -> Kaotik.FRIEND_MANAGER.addFriend(friend.getAsString()));

        stream.close();
    }

    public void saveFriends() throws IOException {
        if (Files.exists(Paths.get("Kaotik/Client/Friends.json"))) {
            File file = new File("Kaotik/Client/Friends.json");
            file.delete();
        }

        Files.createFile(Paths.get("Kaotik/Client/Friends.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();

        for (Friend friend : Kaotik.FRIEND_MANAGER.getFriends()) friendArray.add(friend.getName());
        mainObject.add("Friends", friendArray);

        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("Kaotik/Client/Friends.json"), StandardCharsets.UTF_8);
        writer.write(gson.toJson(new JsonParser().parse(mainObject.toString())));
        writer.close();
    }

    public static class SaveThread extends Thread {
        @Override
        public void run(){
            Kaotik.CONFIG_MANAGER.save();
        }
    }
}
