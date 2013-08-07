package com.agun.flyJenkins.ui.ConfigServiceMeta;

import lib.JenkinsTagLib
import lib.FormTagLib

def f=namespace(FormTagLib.class)

t=namespace(JenkinsTagLib.class)

namespace("/lib/samples").sample(title:_("Context-sensitive form validation")) {
      f.form(method : "post", action : "save") {
        f.entry(title:"Service Meta") {
            f.repeatableProperty(field:"serverMetaList")
        }
    }
}
