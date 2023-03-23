const express = require('express')
const fs = require('fs')
const path = require('path')
const app = express()

app.get('/', (req, res) => {
  res.send('Hello World')
})

app.get('/files', async (req, res) => {
  const filePath = path.join(__dirname, "/data/test_file.txt")
  var stat = fs.statSync(filePath);
  
  res.writeHead(200, {
    'Content-Type': 'text/plain',
    'Content-Length': stat.size
  });

  const readStream = fs.createReadStream(filePath)
  readStream.pipe(res)
})

app.listen(3000, () => {
  console.log("App listening at port 3000")
})
