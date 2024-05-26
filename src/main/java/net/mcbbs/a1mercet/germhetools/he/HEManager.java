package net.mcbbs.a1mercet.germhetools.he;

import net.mcbbs.a1mercet.germhetools.util.Options;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HEManager
{
    public static String getFileNameOnly(String v)
    {
        if(!v.contains("."))return v;
        StringBuilder nameBuilder = new StringBuilder();
        String[] namrAry = v.split("\\.");
        for(int i = 0;i<namrAry.length-1;i++)
        {
            nameBuilder.append(namrAry[i]);
            if(i<namrAry.length-2)nameBuilder.append(".");
        }
        return nameBuilder.toString();
    }
    public static String covertFilePath(String v)
    {
        String[] pathAry = v.split("\\\\");
        StringBuilder path = new StringBuilder();
        int endIndex = pathAry.length;
        for(int i = pathAry.length-1;i>=0;i--)
            if(pathAry[i].contains("."))
            {
                endIndex=i;
                break;
            }
        for(int i = 0;i<endIndex;i++)
            path.append(pathAry[i]).append("/");
        return path.toString();
    }
    public static String convertResourcePath(String v)
    {
        String[] pathAry = v.split("\\\\");
        StringBuilder path = new StringBuilder();

        boolean start = false;

        String nameSpace = "";
        boolean nameSpaceCheck = false;

        for(int i = 0;i<pathAry.length;i++)
        {
            String s = pathAry[i];
            if(start)
            {
                path.append(s);
                if(i<pathAry.length-1)path.append("/");
            }

            if(nameSpaceCheck){nameSpaceCheck=false;nameSpace=s;}
            if(s.equalsIgnoreCase("assets")) {start=true;nameSpaceCheck=true;}

        }

        return path.toString().replace(nameSpace+"/",nameSpace+":").toLowerCase();
    }

    public static List<String> generateMaterial(String folder,List<String> exclude)
    {
        Bukkit.getLogger().info("生成材质:"+folder+" "+exclude.toString());

        List<String> list = new ArrayList<>();
        File file = new File(folder);   if(!file.isDirectory()){Bukkit.getLogger().info("生成失败 - 目标路径非文件夹");return list;}
        File[] ary = file.listFiles();  if(ary==null) {Bukkit.getLogger().info("生成失败 - 获取子文件失败");return list;}

        for(File f : ary)
        {

            if(!f.getName().contains("."))continue;

            boolean cancelle = false;
            for(String v: exclude) if(f.getName().contains(v)) {cancelle=true;break;}
            if(cancelle)continue;

            list.add(f.getName().split("\\.")[0]+":"+ convertResourcePath(f.getPath()));
            Bukkit.getLogger().info("GenerateMaterial:"+list.get(list.size()-1));
        }

        return list;
    }

    public static List<String> generateMaterial(File mtl,String path)
    {
        Bukkit.getLogger().info("生成材质:"+mtl.getPath());

        List<String> list = new ArrayList<>();
        BufferedReader reader;
        try {

            reader = new BufferedReader(new FileReader(mtl));
            String line = reader.readLine();

            String materialName="";

            while (line!=null){

                String[] ary = line.split(" ");
                if(ary.length>0){

                    if(ary[0].equalsIgnoreCase("newmtl")) {
                        materialName=ary[1];
                    } else if(ary[0].equalsIgnoreCase("map_Kd")) {
                        String name = ary[ary.length-1];
                        if(name.contains("/"))
                        {
                            String[] namerAry = name.split("/");
                            name=namerAry[namerAry.length-1];
                        }

                        list.add(materialName+":"+ convertResourcePath(path)+"/"+name.toLowerCase());
                        Bukkit.getLogger().info("GenerateMaterial:"+list.get(list.size()-1));
                    }

                }

                line=reader.readLine();
            }

        }catch (IOException e){e.printStackTrace();}
        return list;
    }

    public static List<String> modelTypes = Arrays.asList(".obj",".gltf",".glb",".fbx");

    /**
     * <br>读取同名的MTL文件生成材质(默认true)
     * <br>[Boolean/String]..READ_MTL
     * <br>
     * <br>
     * <br>生成的HEState默认带多少个空材质槽
     * <br>[int/String]......CREATE_EMPTY_TEXTURE
     * <br>
     * <br>
     * <br>重定向模型路径
     * <br>-留空按文件所在路径
     * <br>[String]..........MODEL_PATH
     * <br>
     * <br>
     * <br>重定向纹理路径
     * <br>-留空按文件所在路径
     * <br>-NONE不生成纹理路径
     * <br>[String]..........TEXTURE_PATH
     * <br>
     * <br>
     * <br>纹理文件格式 默认png
     * <br>[STRING]..........IMAGE_TYPE
     */
    public static List<HEState> modelToPreset(String input, String output, Options<String> parameters)
    {
        List<HEState> list = new ArrayList<>();
        File targetFile = new File(output);
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(targetFile);

        File file = new File(input);    if(!file.isDirectory()){Bukkit.getLogger().info("生成失败 - 输入路径非文件夹");return list;}
        File[] ary = file.listFiles();  if(ary==null) {Bukkit.getLogger().info("生成失败 - 获取子文件失败");return list;}

        int CREATE_EMPTY_TEXTURE = parameters.containsKey("CREATE_EMPTY_TEXTURE")?Integer.parseInt(parameters.getString("CREATE_EMPTY_TEXTURE")):0;
        String MODEL_PATH   = parameters.getString("MODEL_PATH",null);
        String TEXTURE_PATH = parameters.getString("TEXTURE_PATH",null);
        String IMAGE_TYPE   = parameters.getString("IMAGE_TYPE",null);
        boolean READ_MTL    = parameters.getBoolean("READ_MTL",true);

        Bukkit.getLogger().info("生成模型文件预设:");
        Bukkit.getLogger().info("参数:");
        Bukkit.getLogger().info("--CREATE_EMPTY_TEXTURE: "+CREATE_EMPTY_TEXTURE);
        Bukkit.getLogger().info("--MODEL_PATH:           "+MODEL_PATH);
        Bukkit.getLogger().info("--TEXTURE_PATH:         "+TEXTURE_PATH);
        Bukkit.getLogger().info("--IMAGE_TYPE:           "+IMAGE_TYPE);
        Bukkit.getLogger().info("--READ_MTL:             "+READ_MTL);

        StringBuilder builder = new StringBuilder();

        for (File model : ary)
        {
            try {
                if(model.getName().contains(".mtl"))continue;
                String name = getFileNameOnly(model.getName());
                builder.append("\n----").append(name).append("[").append(model.getName()).append("]");

                HEState state = new HEState();
                state.setSize(10D);
                state.setAABBMax(1D,1D,1D);
                state.setTransform(0.5D,0D,0.5D);
                state.setScale(1D,1D,1D);

                state.id = name;
                state.name = name;

                String path = model.getPath().replace(model.getName(),"");
                String convertPath  = convertResourcePath(model.getAbsolutePath());

                state.model     = MODEL_PATH   == null ? convertPath : (MODEL_PATH+"/")   + model.getName();
                state.texture   = TEXTURE_PATH == null ? (convertPath.replace(model.getName(),"")+name) : (TEXTURE_PATH+"/") + name;
                state.texture  += IMAGE_TYPE   == null ? ".png" : ("."+IMAGE_TYPE);
                builder.append("\n------模型文件 ").append(state.model);
                builder.append("\n------纹理文件 ").append(state.texture);

                if(READ_MTL)
                {
                    File mtlFile = new File(path+"/"+name+".mtl");
                    if(mtlFile.exists()){
                        List<String> mtl = generateMaterial(mtlFile,TEXTURE_PATH==null?(path+"texture"):TEXTURE_PATH+"/");
                        state.material.addAll(mtl);
                        Bukkit.getLogger().info("\n---------已读取mtl["+mtl.size()+"]");
                    }else {
                        Bukkit.getLogger().info("\n"+name+".mtl不存在 跳过读取");
                    }
                }

                if (CREATE_EMPTY_TEXTURE > 0)
                {
                    for (int j = 0; j < CREATE_EMPTY_TEXTURE; j++)
                        state.material.add(":");
                    builder.append("\n--------空材质槽 ").append(CREATE_EMPTY_TEXTURE);
                }

                list.add(state);
            }catch (Exception e){Bukkit.getLogger().warning(model.getName()+" 生成出错");e.printStackTrace();}
        }

        Bukkit.getLogger().info(builder.toString());

        cfg.set("Generate.AssetsLibrary.Fold.0.Preset",null);

        for(int i = 0;i<list.size();i++)
        {
            HEState state = list.get(i);
            state.save(cfg.createSection("Generate.AssetsLibrary.Fold.0.Preset."+i));
        }
        Bukkit.getLogger().info("已完成 "+list.size()+"个预设创建 输出路径 "+targetFile.getAbsolutePath());

        try {cfg.save(targetFile);} catch (Exception e) {e.printStackTrace();}
        return list;

    }
}
