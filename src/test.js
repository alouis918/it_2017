'use strict'
function LineDrawer(canvas) {
    this.canvas = canvas;
    this.prevX = 0;
    this.prevY = 0;
    this.currX = 0;
    this.currY = 0;
    this.flag = false;
    this.dot_flag = false;
    this.ctx = this.canvas.getContext("2d");
    this.strokeStyle = "black";
    this.lineWidth = 5;
    this.addEventListener();
}

LineDrawer.prototype.addEventListener = function () {
    var _this = this;
    this.canvas.addEventListener("click", function (event) {
        _this.getCoordinates("click", event);
    }, false);
    this.canvas.addEventListener("mouseup", function (event) {
        _this.getCoordinates("up", event);
    }, false);

     this.canvas.addEventListener("mousemove", function (event) {
         _this.getCoordinates("move", event);
     }, false);
     this.canvas.addEventListener("mousedown", function (event) {
         _this.getCoordinates("down", event);
     }, false);
        /*
     this.canvas.addEventListener("mouseout", function (event) {
         _this.getCoordinates("out", event);
     }, false);*/
};
LineDrawer.prototype.drawLine = function () {
    this.ctx.beginPath();
    this.ctx.moveTo(this.prevX, this.prevY);
    this.ctx.lineTo(this.currX, this.currY);
    this.ctx.strokeStyle = this.strokeStyle;
    this.ctx.lineWidth = this.lineWidth;
    this.ctx.stroke();
    this.ctx.closePath();
};
LineDrawer.prototype.getCoordinates = function (action, event) {
    switch(action){
        case "down":
            /*
            this.prevX = this.currX;
            this.prevY = this.currY;
            */
            this.prevX = event.clientX - this.canvas.offsetLeft;
            this.prevY = event.clientY - this.canvas.offsetTop;
            /*
            this.flag = true;
            if (this.dot_flag) {
                this.ctx.beginPath();
                this.ctx.fillStyle = this.strokeStyle;
                this.ctx.fillRect(this.currX, this.currY, 2, 2);
                this.ctx.closePath();
                this.dot_flag = false;
            }
            */
            break;
        case "move":
            /*
            this.prevX = this.currX;
            this.prevY = this.currY;
            */
            this.currX = event.clientX - this.canvas.offsetLeft;
            this.currY = event.clientY - this.canvas.offsetTop;
            // this.drawLine();
            break;
        case "click":
            this.prevX = this.currX;
            this.prevY = this.currY;
            this.currX = event.clientX - this.canvas.offsetLeft;
            this.currY = event.clientY - this.canvas.offsetTop;
           // this.drawLine();
            break;
        case "up":
            this.drawLine();
        case "out":
            this.flag = false;
            break;
    }
};
