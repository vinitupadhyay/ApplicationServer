package com.gilbarco.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.gilbarco.model.connection.SpotConnection;
import com.gilbarco.pojo.SPOTResponse;

import troubleshoot.controller.TroubleshootController;

@RestController
public class MaintenanceController 
{
	/*@Autowired
	private TroubleshootController controller;*/
	
    /*@CrossOrigin
    @GetMapping("/maintenance")
    public SPOTResponse connect(@RequestParam(required=true) String ip, @RequestParam(required=true) int port)
    {
    	boolean flag = controller.doConnectOnceOnly();
		if(flag)
		{
			return new SPOTResponse("connect", "true");
		}
		else
		{
			return new SPOTResponse("connect", "false");
		}
        
    }*/
    
    @CrossOrigin
    @GetMapping("/maintenance")
    public SPOTResponse connect(@RequestParam(required=true) String ip, @RequestParam(required=true) int port)
    {
    	boolean flag = new SpotConnection().doConnectTLS(ip, port);
		if(flag)
		{
			return new SPOTResponse("connect", "true");
		}
		else
		{
			return new SPOTResponse("connect", "false");
		}
        
    }
	
	/*@CrossOrigin
    @GetMapping("/maintenance")
    public Map connect(@RequestParam(required=true) String ip, @RequestParam(required=true) int port)
    {
    	boolean flag = new SpotConnection().doConnectTLS(ip, port);
		if(flag)
		{
			Map m = new HashMap<>();
			m.put("connect", "true");
			return m;
		}
		else
		{
			Map m = new HashMap<>();
			m.put("connect", "false");
			return m;
		}
        
    }*/

    @GetMapping("/maintenance-config")
    public SPOTResponse connectconfig(@RequestParam(required=true) String ip, @RequestParam(required=true) int port)
    {
    	boolean flag = new SpotConnection().doConnectTLS(ip, port);
		if(flag)
		{
			return new SPOTResponse("connect", "true");
		}
		else
		{
			return new SPOTResponse("connect", "false");
		}
    }

}
