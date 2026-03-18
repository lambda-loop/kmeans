
{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  name = "haskell-sdl2-env";

  buildInputs = with pkgs; [
    # ghc                  
    # cabal-install        
    # haskell-language-server 

    pkg-config
    
    haskellPackages.hmatrix
    gfortran      
    blas          
    lapack        
    zlib          
    gmp           

    haskellPackages.ghc-prof-flamegraph
    haskellPackages.profiteur
    ghostscript  
    linuxPackages.perf  
  ];

  shellHook = ''
    export LD_LIBRARY_PATH="${pkgs.blas}/lib:${pkgs.lapack}/lib:$LD_LIBRARY_PATH"
    export LIBRARY_PATH="${pkgs.blas}/lib:${pkgs.lapack}/lib:$LIBRARY_PATH"  # para linkagem
  '';

}
