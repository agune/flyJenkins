package com.agun.flyJenkins.ui.ConfigServiceMeta;

import lib.JenkinsTagLib
import lib.FormTagLib

def f=namespace(FormTagLib.class)

t=namespace(JenkinsTagLib.class)


namespace("/lib/samples").sample(title:_("Setting Service Meta Info")) {

div(style:"padding-bottom: 1em"){
	button(onclick:"location.href='../NetworkInfo'", "network group")
}
      
	div (style:"border:1px solid blue") {
 		for(serverMeta in instance.getServerMetaList()){
 			p(){
 				span(style: "padding:2em" ,  serverMeta.host)
 				button(onclick:"location.href='../ProcessInfo?host=" + serverMeta.host+"&servicePid=" + serverMeta.pid  + "'", "process")
 				if(serverMeta.pid > 0){
 					image(src:"/jenkins/static/1d061002/images/32x32/blue.png")
 				}else{
 					image(src:"/jenkins/static/1d061002/images/32x32/red.png")
 				}
 			}
 		}
	}
      
 	f.form(method : "post", action : "save") {
		f.entry() {
			f.entry(title:"host", field:"host") {
    			f.textbox()
			}

			f.entry(title:"destination", field:"destination") {
		    	f.textbox()
			}
		
			f.entry(title:"command", field:"testCmd") {
		    	f.textbox()
			}
		
			f.entry(title:"testUrl", field:"testUrl") {
			    f.textbox()
			}
			
			f.entry(title:"weight", field:"weight") {
			    f.textbox()
			}
			
			f.entry(title:"serverId", field:"serverId") {
			    f.textbox()
			}
			
			f.entry(title:"groupId", field:"groupId") {
			    f.textbox()
			}
			
			f.entry(title:"type", field:"type") {
			    f.select()
			}
		
			f.bottomButtonBar(){
				f.submit(value:"save")
			}
        }
    }
}
