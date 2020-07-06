package online.smyhw.EschatologicalUnit.TimeCmd;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class smyhw extends JavaPlugin implements Listener 
{
	public static Plugin smyhw_;
	public static Logger loger;
	public static FileConfiguration configer;
	public static String prefix;
	public static List<DoCmd> CmdList = Collections.synchronizedList(new ArrayList<DoCmd>());
	@Override
    public void onEnable() 
	{
		getLogger().info("EschatologicalUnit.TimeCmd加载");
		getLogger().info("正在加载环境...");
		loger=getLogger();
		configer = getConfig();
		smyhw_=this;
		getLogger().info("正在加载配置...");
		saveDefaultConfig();
		prefix = configer.getString("config.prefix");
		getLogger().info("正在注册监听器...");
		Bukkit.getPluginManager().registerEvents(this,this);
		getLogger().info("EschatologicalUnit.TimeCmd加载完成");
    }

	@Override
    public void onDisable() 
	{
		getLogger().info("EschatologicalUnit.TimeCmd卸载");
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
        if (cmd.getName().equals("euT"))
        {
                if(!sender.hasPermission("eu.plugin")) 
                {
                	sender.sendMessage(prefix+"非法使用 | 使用者信息已记录，此事将被上报");
                	loger.warning(prefix+"使用者<"+sender.getName()+">试图非法使用指令<"+args+">{权限不足}");
                	return true;
                }
                if(args.length<2) 
                {
                	CSBZ(sender);
                	return true;
                }
                switch(args[0])
                {
                case "reset":
                {
                	for(DoCmd temp1:CmdList)
                	{
                		temp1.cancel();
                		CmdList.remove(temp1);
                	}
                	sender.sendMessage(prefix+"序列中的指令已清除");
                	return true;
                }
                case"run":
                {
                	String[] temp1 = Arrays.copyOfRange(args, 2,args.length);
                	String runCmd="";
                	for(String temp2:temp1)
                	{
                		runCmd = runCmd+temp2+" ";
                	}
                	long time  = Long.valueOf(args[1]);
                	new DoCmd(runCmd,time);
                	sender.sendMessage(prefix+"指令<"+runCmd+">定时<"+time/20+">秒后执行");
                	return true;
                }
                default:
                	CSBZ(sender);
                }
                return true;                                                       
        }
       return false;
	}
	
	static void CSBZ(CommandSender sender)
	{
		sender.sendMessage(prefix+"非法使用 | 使用者信息已记录，此事将被上报");
		loger.warning(prefix+"使用者<"+sender.getName()+">试图非法使用指令{参数不足}");
	}
	
}



class DoCmd extends BukkitRunnable
{
	String cmd;
	public DoCmd(String cmd,long time)
	{
		this.cmd = cmd;
		smyhw.CmdList.add(this);
		this.runTaskLater(smyhw.smyhw_, time);
	}
	
	@Override
	public void run() 
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
		smyhw.CmdList.remove(this);
	}
}