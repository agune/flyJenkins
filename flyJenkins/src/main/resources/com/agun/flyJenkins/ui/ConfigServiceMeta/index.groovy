package com.agun.flyJenkins.ui.ConfigServiceMeta;

import lib.JenkinsTagLib
import lib.FormTagLib

def f=namespace(FormTagLib.class)

t=namespace(JenkinsTagLib.class)

namespace("/lib/samples").sample(title:_("Context-sensitive form validation")) {
      
	div (style:"border:1px solid blue") {
 		for(serverMeta in instance.getServerMetaList()){
 			p(serverMeta.host)
 		}
	}
      
 	f.form(method : "post", action : "save") {
		f.entry(title:"Service Meta") {
			f.entry(title:"host", field:"host") {
    			f.textbox()
			}

			f.entry(title:"destination", field:"destination") {
		    	f.textbox()
			}
		
			f.entry(title:"testCmd", field:"testCmd") {
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
			    f.textbox()
			}
		
			f.bottomButtonBar(){
				f.submit(value:"save")
			}
        }
    }
}
