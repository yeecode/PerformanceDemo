const http = require('http')
const fs = require('fs')

http.createServer(function (request, response) {
    console.log('request :', request.url)
    const image = fs.readFileSync('./01.jpg');
    if (request.url === '/02') {
        response.writeHead(200, {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'image/jpg',
            'Connection': 'close'
        })
    } else {
        response.writeHead(200, {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'image/jpg',
            'Connection': 'keep-alive'
        })
    }
    response.end(image)
}).listen(8888)