package net.mcbbs.a1mercet.germhetools.he;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HEManager
{
    public static String convertPath(String v)
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

            list.add(f.getName().split("\\.")[0]+":"+convertPath(f.getPath()));
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

                    if(ary[0].equalsIgnoreCase("newmtl"))
                    {
                        materialName=ary[1];
                    } else if(ary[0].equalsIgnoreCase("map_Kd")) {
                        String name = ary[1];
                        if(name.contains("/"))
                        {
                            String[] namrAry = name.split("/");
                            name=namrAry[namrAry.length-1];
                        }

                        list.add(materialName+":"+convertPath(path)+"/"+name.toLowerCase());
                        Bukkit.getLogger().warning("GenerateMaterial:"+list.get(list.size()-1));
                    }

                }

                line=reader.readLine();
            }

        }catch (IOException e){e.printStackTrace();}
        return list;
    }
}
