<?php
$first_domain_word=explode(".",$_SERVER["HTTP_HOST"])[0];
$number_fragment_charsfragment=4;
$success=false;
?>
<html>
	<head>
		<title>Anti Phishing Education</title>
	</head>
	<body>
<?php
if(isset($_REQUEST["first_word"])){
if($_REQUEST["first_word"]==$first_domain_word && $_REQUEST["chars"] == substr($_REQUEST["fragment"],-$number_fragment_charsfragment)){
  $success=true;
?>
<p>Super! Hier geht es <a href="phishedu://level1finished/">zur&uuml;ck zur App</a></p>
<?php
}else{
?>
<p>Leider ist deine Antwort falsch.</p>
<p>Du kannst es entweder nochmal probieren. L&auml;sst es dir <a href="phishedu://level1failed/">noch einmal erkl&auml;ren</a>.</p>
<?php
}
}

if(!$success){
?>
<form action="<?= $_SERVER["REQUEST_URI"] ?>" method="post">
<p>1. Nun schaue dir die gesamte Webadresse an. Hierzu musst du den Adresstext antippen und mit dem Finger ggf. nach rechts und links schieben.</p>
<p>2. Gebe die letzten <?= $number_fragment_charsfragment ?> Zeichen der Webadresse ein:</p>
<input type="text" name="chars" />
<p>3. Welches der folgenden Begriffe ist das erste Wort der Webadresse:</p>
<ul style="list-style:none;padding-left:0px;">
<li><input type="radio" name="first_word" value="bottom" />bottom</li>
<li><input type="radio" name="first_word" value="no-phish" />no-phish</li>
<li><input type="radio" name="first_word" value="<?=$first_domain_word?>" /><?=$first_domain_word?></li>
<li><input type="radio" name="first_word" value="de" />de</li>
</ul>
<input type="hidden" name="fragment" value="<?= $_REQUEST["frag"] ?>" />
<p><input type="submit" /></p>
                        <div style="height:110%;">&nbsp;</div>
			<p>Bitte scrolle ganz nach oben, um dir die Webadresse anzeigen zu lassen. Ziehe hierzu den Finger so lange herunter (ggf. mehrmals), bis du die Adressleiste siehst.</p>
                        <a name="bottom-<?= $_REQUEST["frag"] ?>" />
</form>
<?php
}
?>
			</body>
		</html>
