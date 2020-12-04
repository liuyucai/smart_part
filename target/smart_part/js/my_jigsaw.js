
function getRandomNumberByRange (start, end) {
    return Math.round(Math.random() * (end - start) + start)
}

function createCanvas (width, height) {
    const canvas = document.createElement('canvas')
    canvas.width = width
    canvas.height = height
    return canvas
}

function createImg (onload) {
    const img = new Image()
    img.crossOrigin = 'Anonymous'
    img.onload = onload
    img.onerror = () => {
        img.setSrc(getRandomImgSrc()) // 图片加载失败的时候重新加载其他图片
    }

    img.setSrc = function (src) {
        const isIE = window.navigator.userAgent.indexOf('Trident') > -1
        if (isIE) { // IE浏览器无法通过img.crossOrigin跨域，使用ajax获取图片blob然后转为dataURL显示
            const xhr = new XMLHttpRequest()
            xhr.onloadend = function (e) {
                const file = new FileReader() // FileReader仅支持IE10+
                file.readAsDataURL(e.target.response)
                file.onloadend = function (e) {
                    img.src = e.target.result
                }
            }
            xhr.open('GET', src)
            xhr.responseType = 'blob'
            xhr.send()
        }
        else img.src = src
    }

    img.setSrc(getRandomImgSrc())
    return img
}

function createElement (tagName, className) {
    const element = document.createElement(tagName)
    // className && (element.className = styles[className])
    className && (element.className = className)
    return element
}

function setClass (element, className) {
    // element.className = styles[className]
    element.className = className
}

function addClass (element, className) {
    // element.classList.add(styles[className])
    element.classList.add(className)
}

function removeClass (element, className) {
    // element.classList.remove(styles[className])
    element.classList.remove(className)
}

function getRandomImgSrc () {
    return `https://picsum.photos/id/${getRandomNumberByRange(0, 1084)}/${w}/${h}`
}

function drawPath (ctx, x, y, operation) {
    ctx.beginPath()
    ctx.moveTo(x, y)
    ctx.arc(x + l / 2, y - r + 2, r, 0.72 * PI, 2.26 * PI)
    ctx.lineTo(x + l, y)
    ctx.arc(x + l + r - 2, y + l / 2, r, 1.21 * PI, 2.78 * PI)
    ctx.lineTo(x + l, y + l)
    ctx.lineTo(x, y + l)
    ctx.arc(x + r - 2, y + l / 2, r + 0.4, 2.76 * PI, 1.24 * PI, true)
    ctx.lineTo(x, y)
    ctx.lineWidth = 2
    ctx.fillStyle = 'rgba(255, 255, 255, 0.7)'
    ctx.strokeStyle = 'rgba(255, 255, 255, 0.7)'
    ctx.stroke()
    ctx.globalCompositeOperation = 'destination-over'
    operation === 'fill'? ctx.fill() : ctx.clip()
}

function sum (x, y) {
    return x + y
}

function square (x) {
    return x * x
}

function getImage (w,h,bigImg,slideImg){

    var xmlhttp;
    if (window.XMLHttpRequest)
    {
        //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // IE6, IE5 浏览器执行代码
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var aa = xmlhttp.responseText;
            var result = JSON.parse(aa);
            console.log(result);
            // bigImg.src = result.data.bigImg;
            // slideImg.src = result.data.slidImg;
            bigImg.src = "data:image/jpeg;base64,"+result.bigImg;
            slideImg.src = "data:image/jpeg;base64,"+result.slidImg;
        }
    }
    xmlhttp.open("POST","/imageAuthentication/getImage",true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("width="+w+"&height="+h);
}

class my_Jigsaw {
    constructor ({ el, option, onSuccess, onFail, onRefresh }) {
        //给目标元素设置样式，es6语法
        Object.assign(el.style, {
            position: 'relative',
            width: option.width + 'px',
            height: option.height + 'px',
            margin: '0 auto'
        })
        this.width = option.width
        this.height = option.height
        this.el = el
        this.onSuccess = onSuccess
        this.onFail = onFail
        this.onRefresh = onRefresh

        this.init()
    }

    init () {
        this.initDOM()
        this.initImg()
        // this.bindEvents()
    }

    initDOM () {
        const { width, height } = this  //es6语法


        //图片内容显示区域
        const imageContainer = createElement('div', 'imageContainer')
        const bigImg = createElement('img', 'bigImg')
        const slideImg = createElement('img', 'slideImg')

        var imageContainerHeight = (this.height -15)*0.8;

        var silderHeight = (this.height -10)*0.2;

        // setClass(imageContainer, 'block')
        setClass(slideImg, 'block')

        //给目标元素设置样式，es6语法
        Object.assign(imageContainer.style, {
            position: 'relative',
            width: this.width + 'px',
            height: imageContainerHeight + 'px',
        })
        imageContainer.appendChild(bigImg)
        imageContainer.appendChild(slideImg)

        //滑块显示区域
        const sliderContainer = createElement('div', 'sliderContainer')
        sliderContainer.style.width = width + 'px'

        sliderContainer.style.height = silderHeight + 'px'

        sliderContainer.style.pointerEvents = 'none'
        const refreshIcon = createElement('div', 'refreshIcon')
        const sliderMask = createElement('div', 'sliderMask')
        const slider = createElement('div', 'slider')

        sliderMask.style.height = silderHeight + 'px'
        slider.style.height = silderHeight + 'px'
        slider.style.width = silderHeight + 'px'

        const sliderIcon = createElement('span', 'sliderIcon')
        const text = createElement('span', 'sliderText')
        text.innerHTML = '向右滑动填充拼图'

        // 增加loading
        const loadingContainer = createElement('div', 'loadingContainer')
        loadingContainer.style.width = width + 'px'
        loadingContainer.style.height = height + 'px'
        const loadingIcon = createElement('div', 'loadingIcon')
        const loadingText = createElement('span')
        loadingText.innerHTML = '加载中...'
        loadingContainer.appendChild(loadingIcon)
        loadingContainer.appendChild(loadingText)

        const el = this.el
        el.appendChild(loadingContainer)
        // el.appendChild(canvas)
        el.appendChild(imageContainer)
        el.appendChild(refreshIcon)
        // el.appendChild(block)

        slider.appendChild(sliderIcon)
        sliderMask.appendChild(slider)
        sliderContainer.appendChild(sliderMask)
        sliderContainer.appendChild(text)
        el.appendChild(sliderContainer)

        Object.assign(this, {
            sliderContainer,
            loadingContainer,
            imageContainer,
            refreshIcon,
            slider,
            sliderMask,
            sliderIcon,
            text
        })

        getImage(this.width,imageContainerHeight,bigImg,slideImg);
        // bigImg.src="";

    }

    setLoading (isLoading) {
        this.loadingContainer.style.display = isLoading ? '' : 'none'
        this.sliderContainer.style.pointerEvents = isLoading ? 'none' : ''
    }

    initImg () {
        const img = createImg(() => {
            this.setLoading(false)
            // this.draw(img)
        })
        this.img = img
        // var imgjson = getImage();
    }

    verify () {
        const arr = this.trail // 拖动时y轴的移动距离
        const average = arr.reduce(sum) / arr.length
        const deviations = arr.map(x => x - average)
        const stddev = Math.sqrt(deviations.map(square).reduce(sum) / arr.length)
        const left = parseInt(this.block.style.left)
        return {
            spliced: Math.abs(left - this.x) < 10,
            verified: stddev !== 0, // 简单验证拖动轨迹，为零时表示Y轴上下没有波动，可能非人为操作
        }
    }

    reset () {
        const { width, height } = this
        // 重置样式
        setClass(this.sliderContainer, 'sliderContainer')
        this.slider.style.left = 0 + 'px'
        this.block.width = width
        this.block.style.left = 0 + 'px'
        this.sliderMask.style.width = 0 + 'px'

        // 清空画布
        this.canvasCtx.clearRect(0, 0, width, height)
        this.blockCtx.clearRect(0, 0, width, height)

        // 重新加载图片
        this.setLoading(true)
        this.img.setSrc(getRandomImgSrc())
    }
}

// window.my_Jigsaw = {
//     init: function (opts) {
//         return new Jigsaw(opts).init()
//     }
// }