$ my example 3
fun whileexample () : int
var x : int;
{
  $$i<-0;
  if i < 1 then 
    while i < 5 do
      i <-i+1;
  else
    return -1;$$

  $$x <- 5;
  if x < 6 then
    while x > 30 do
      if x > 5 then x <-4;
      else x <- x + 1;$$
    x <- 3;
  if 1 < 2 then
    while x < 30 do
    if x < 3 then
      while x < 20 do
        x <- x+3;
    else
      while x < 25 do
        x <- x+4;
  else if 4 > 5 then
    puts("impossible");
  else
    puts("wrong");
  return 0;
}
