/* Author: Markus Henn */

var cdsCount = 0;
var tracksCount = new Array();

window.onload = function() {
	var editalbumCdsElement = document.getElementById('editalbum_cds');
	if (editalbumCdsElement != null) {
		addNewCdInput(editalbumCdsElement);
	}
};

function addNewCdInput(parent) {
	tracksCount.push(0);
	cdsCount++;

	var newCdInput = document.createElement("fieldset");
	newCdInput.setAttribute("class", "editalbum_cd");
	parent.appendChild(newCdInput);
	
	var legend = document.createElement("legend");
	newCdInput.appendChild(legend);
	
	var legendText = document.createTextNode("CD " + cdsCount);
	legend.appendChild(legendText);
	
	var tracksElement = document.createElement("div");
	var tracksElementId = "editalbum_cd_" + cdsCount + "_tracks";
	tracksElement.setAttribute("id", tracksElementId);
	newCdInput.appendChild(tracksElement);
	
	addNewTrackInput(tracksElement, cdsCount);
	
	/*
	 * Extra button to add new track (replaced by onchange-Event of trackInput)
	var addTrackElement = document.createElement("div");
	newCdInput.appendChild(addTrackElement);
	
	var addTrackElementButton = document.createElement("button");
	addTrackElementButton.setAttribute("onclick", "addNewTrackInput(document.getElementById('" + tracksElementId + "'), cdsCount); return false;");
	addTrackElement.appendChild(addTrackElementButton);
	
	var addTrackElementButtonText = document.createTextNode("Weiteren Track hinzufügen…");
	addTrackElementButton.appendChild(addTrackElementButtonText);
	*/
	
	return false;
}

function addNewTrackInput(parent, cdNumber) {
	var cdIndex = cdNumber - 1;
	tracksCount[cdIndex]++;
	
	var trackUpload = document.createElement("div");
	trackUpload.setAttribute("class", "form_input");
	parent.appendChild(trackUpload);
	
	var trackUploadLabel = document.createElement("label");
	trackUpload.appendChild(trackUploadLabel);
	
	var trackNumberText = document.createTextNode(tracksCount[cdIndex]);
	trackUploadLabel.appendChild(trackNumberText);
	
	var trackUploadInput = document.createElement("input");
	trackUploadInput.setAttribute("name", "cd_" + cdNumber + "_tracks[]");
	trackUploadInput.setAttribute("type", "file");
	trackUploadInput.setAttribute("accept", "audio/*");
	
	// html5 multiple file upload, doesn't work in IE<10
	trackUploadInput.setAttribute("multiple", "multiple");
	// so we still add a new input field for ie users to select additional files
	trackUploadInput.onchange = function() { addNewTrackInput(parent, cdNumber); this.onchange = null; };

	trackUpload.appendChild(trackUploadInput);
	
	/*
	var trackUploadButton = document.createElement("button");
	trackUploadButton.setAttribute("onclick", "addNewTrackInput(" + parent + ", cdsCount);");
	trackUpload.appendChild(trackUploadButton);
	
	var trackUploadButtonText = document.createTextNode("Hochladen");
	trackUploadButton.appendChild(trackUploadButtonText);
	*/
	return false;
}