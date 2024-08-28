		$(function(){

/*현재 URL 경로에서 파일명을 추출하여 해당되는 탭을 block하고 해당 순서의 탭에 tab_on 클래스를 추가*/
var link = window.location.pathname;
var page = link.slice(-6);
var val = page.substring(0,2);

var arSplitUrl = link.split("/");    //   "/" 로 전체 url 을 나눈다
var nArLength = arSplitUrl.length;
var arFileName = arSplitUrl[nArLength-1];   // 나누어진 배열의 맨 끝이 파일명이다
var arSplitFileName = arFileName.split(".");   // 파일명을 다시 "." 로 나누면 파일이름과 확장자로 나뉜다
var sFileName = "tab_"+arSplitFileName[0].substring(0,6);         // 파일이름


$('.'+arSplitFileName[0]).parents( '.tabwrap' ).css('display','block');


$('.tt_'+arSplitFileName[0]).parents( '.smenu_wrap' ).css('display','block');

console.log(arSplitFileName[0]);


$('.it_'+arSplitFileName[0]).parents( '.intab_wrap' ).css('display','block');
console.log(arSplitFileName[0]);
$('.'+arSplitFileName[0]).parents( 'div' ).css('display','block');
$('.sm_'+arSplitFileName[0]).parents( 'div' ).css('display','block');


$('.sm_'+arSplitFileName[0]).addClass('smtab_on');

$('.'+arSplitFileName[0]).addClass('intab_on');
$('.'+arSplitFileName[0]).addClass('tab_on');

$('.it_'+arSplitFileName[0]).addClass('it_on');


$('.sm_'+arSplitFileName[0]).parents( '.papa' ).addClass('smtab_on');
})