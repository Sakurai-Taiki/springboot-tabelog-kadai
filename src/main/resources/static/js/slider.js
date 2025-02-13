document.addEventListener("DOMContentLoaded", function() {
  'use strict';
  // 設定オブジェクト（Tabelogサンプルに近い設定）
  var conf = {
    delay: 6000, // 待機時間（ミリ秒）
    speed: 3000, // フェード遷移時間（ミリ秒）
    height: 0,   // 固定高さ（0なら縦横比による）
    ratio: 9/16  // 縦横比（height=0 の場合）
  };
  
  // コンテナーの設定（必要なら JS で高さを設定、ここは既存CSSで管理する前提）
  var container = document.getElementById('imageSlider');
  if (conf.height) {
    container.style.height = conf.height + 'px';
  }
  
  // 使用する画像のパス
  var sliderImages = [
    '/images/slider/slider_01.jpg',
    '/images/slider/slider_02.jpg',
    '/images/slider/slider_03.jpg',
    '/images/slider/slider_04.jpg',
    '/images/slider/slider_05.jpg'
    // 追加画像があればここに
  ];
  
  // 画像要素を生成してコンテナーに追加
  sliderImages.forEach(function(src, index) {
    var img = document.createElement("img");
    img.src = src;
    img.alt = "Slide " + (index + 1);
    img.classList.add("slide");
    if (index === 0) {
      img.classList.add("current"); // 初期状態で最初の画像を表示
    }
    container.appendChild(img);
  });
  
  // 生成されたスライド要素を取得
  var slides = container.querySelectorAll('.slide');
  var numSlides = slides.length;
  var currentIndex = 0;
  
  // 各スライドに transition プロパティを設定（念のため）
  slides.forEach(function(slide) {
    slide.style.transition = conf.speed + "ms ease-in-out";
  });
  
  // 自動スライド実行
  setInterval(function() {
    var nextIndex = (currentIndex + 1) % numSlides;
    // 現在のスライドをフェードアウト
    slides[currentIndex].classList.remove('current');
    // 次のスライドをフェードイン（すでに背景に配置されている）
    slides[nextIndex].classList.add('current');
    currentIndex = nextIndex;
  }, conf.delay + conf.speed);
});