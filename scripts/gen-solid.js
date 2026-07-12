// Generates solid-color 16x16 PNGs for the Excalibur 3D cuboid item model.
const fs = require("fs"), zlib = require("zlib"), path = require("path");
const OUT = path.resolve(__dirname, "../mythical-swords-template-1.20.1/src/main/resources/assets/mythicalswords/textures/item");

const CRC = (() => { const t = new Uint32Array(256); for (let n=0;n<256;n++){let c=n;for(let k=0;k<8;k++)c=c&1?0xedb88320^(c>>>1):c>>>1;t[n]=c>>>0;} return t; })();
function crc32(b){let c=0xffffffff;for(let i=0;i<b.length;i++)c=CRC[(c^b[i])&0xff]^(c>>>8);return (c^0xffffffff)>>>0;}
function chunk(type,data){const len=Buffer.alloc(4);len.writeUInt32BE(data.length,0);const td=Buffer.concat([Buffer.from(type,"ascii"),data]);const crc=Buffer.alloc(4);crc.writeUInt32BE(crc32(td),0);return Buffer.concat([len,td,crc]);}
function png(w,h,rgba){
  const raw=Buffer.alloc(h*(1+w*4));
  for(let y=0;y<h;y++){raw[y*(1+w*4)]=0;for(let x=0;x<w;x++){const o=y*(1+w*4)+1+x*4;raw[o]=rgba[0];raw[o+1]=rgba[1];raw[o+2]=rgba[2];raw[o+3]=rgba[3];}}
  const sig=Buffer.from([137,80,78,71,13,10,26,10]);
  const ihdr=Buffer.alloc(13);ihdr.writeUInt32BE(w,0);ihdr.writeUInt32BE(h,4);ihdr[8]=8;ihdr[9]=6;
  return Buffer.concat([sig,chunk("IHDR",ihdr),chunk("IDAT",zlib.deflateSync(raw,{level:9})),chunk("IEND",Buffer.alloc(0))]);
}
const hex = h => [parseInt(h.slice(1,3),16),parseInt(h.slice(3,5),16),parseInt(h.slice(5,7),16),255];
const files = {
  excalibur_gold:   "#E6B92E",
  excalibur_blue:   "#2E6FE6",
  excalibur_cream:  "#F2E7BE",
  excalibur_silver: "#D7D7CF",
  excalibur_light:  "#7FC8FF",
};
for (const [name,color] of Object.entries(files)) {
  fs.writeFileSync(path.join(OUT, name+".png"), png(16,16,hex(color)));
  console.log("wrote "+name+".png "+color);
}
