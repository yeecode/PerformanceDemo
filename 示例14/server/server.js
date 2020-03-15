const http = require('http');
const fs = require('fs');

http.createServer(function (request, response) {
    console.log('request :', request.url);
    const image = fs.readFileSync('./01.jpg');
    if (request.url === '/01' || request.url === '/01/') {
        response.writeHead(200, {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'image/jpg',
            'Cache-Control':'public,max-age=315360000',
        })
    } else if (request.url === '/02'|| request.url === '/02/'){
        response.writeHead(200, {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'image/jpg',
            'Cache-Control':'no-cache'
        })
    }
    response.end(image)
}).listen(8888);