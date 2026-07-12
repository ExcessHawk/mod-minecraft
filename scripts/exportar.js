var fs = require('fs');
var parseMagicaVoxel = require('parse-magica-voxel');

fs.readFile("./gundnir.vox", function (err, Buffer) {
    if (err) throw err;
    var data = JSON.stringify(parseMagicaVoxel(Buffer), null, 2);
    fs.writeFile("./mythical-swords-template-1.20.1/src/main/resources/assets/mythicalswords/models/item/gungnir_model.json", data, function(err) {
        if (err) throw err;
        console.log("Archivo guardado en models/item/gungnir_model.json");
    });
});
