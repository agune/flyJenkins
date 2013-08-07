package com.agun.flyJenkins.ui.ConfigServiceMeta.State;

def f = namespace(lib.FormTagLib)

f.entry(title:"host", field:"host") {
    f.textbox()
}

f.entry(title:"destination", field:"destination") {
    f.textbox()
}

f.entry(title:"testCmd", field:"testCmd") {
    f.textbox()
}

f.entry(title:"weight", field:"weight") {
    f.textbox()
}

f.bottomButtonBar(){
	f.submit(value:"save")
}