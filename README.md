# The Odyssey

## A general purpose compiler **and** interpreter for the Odyssey language

Compile from Odyssey lang to various mainstream languages --or interpret it on the fly.

Languages currently supported
* Ruby
* Python


## FizzBuzz example

    let counter be 1

    while counter <= 100 do
        if counter modulus 15 is 0 do
            output "FizzBuzz"
        else
                if counter modulus 3 is 0 do
                    output "Fizz"
                else
                    if counter modulus 5 is 0 do
                        output "Buzz"
                    else
                        output counter
                    end
                end
        end

        let counter be counter plus 1
    end
