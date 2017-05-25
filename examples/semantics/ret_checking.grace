fun main () : nothing

   fun putArray (ref msg : int[]; n : int; ref x : int[]) : char
      var i : int;
   {
	msg <- msg + n;
      puts(msg);
      i <- 0;
      while i < n do {
        if i > 0 then writeString(", ");
        puti(x[i]);
        i <- i+1;
      }
      puts("\n");
	return i; $$ must send error $$
   }
{
	puts("hi\n");
}
